package com.eyas.framework.config;

import com.eyas.framework.EmptyUtil;
import com.eyas.framework.GsonUtil;
import com.eyas.framework.data.EyasFrameworkDto;
import com.eyas.framework.utils.TenantThreadLocal;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Intercepts(
        value = {
                @Signature(
                        type = StatementHandler.class,
                        method = "prepare",
                        args = {Connection.class, Integer.class}
                ),
                @Signature(
                        type = Executor.class,
                        method = "query",
                        args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(
                        type = ParameterHandler.class,
                        method = "setParameters",
                        args = {PreparedStatement.class})
        })
@Slf4j
public class MySqlInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Object target = invocation.getTarget();
        if (target instanceof Executor) {
            result = invocation.proceed(); // 执行请求方法，并将所得结果保存到result中
            if (result instanceof List){
                List<Object> list = (List<Object>) result;
                log.info("<== Total:" + list.size());
            }
        }else if(target instanceof StatementHandler){
            StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            EmptyUtil.dealEmptyDataReturn(statements.getStatements(), "mysql插件异常，statements.getStatements()为空");
            EmptyUtil.dealEmptyDataReturn(statements.getStatements().get(0), "mysql插件异常，statements.getStatements().get(0)为空");
            Statement statement = (Statement)statements.getStatements().get(0);
            String newSql = this.rooter(statement, boundSql, metaObject);
            metaObject.setValue("delegate.boundSql.sql", newSql);
            result = invocation.proceed();
        }
        else if(target instanceof ParameterHandler){
            result = invocation.proceed();
            ParameterHandler parameterHandler = (ParameterHandler)invocation.getTarget();
            result = invocation.proceed();
        }



        return result;
    }


    public String rooter(Statement statement, BoundSql boundSql, MetaObject metaObject) throws Throwable {
        if (statement instanceof Select) {
            return this.processSelect((Select)statement, metaObject);
        } else {
            return statement instanceof Insert ? this.processInsert((Insert)statement, boundSql, metaObject) : boundSql.getSql();
        }
    }

    public String processSelect(Select select, MetaObject metaObject) throws Throwable {
        StringBuffer whereSql = new StringBuffer();
        PlainSelect plain = (PlainSelect)select.getSelectBody();
        EyasFrameworkDto systemUser = (EyasFrameworkDto) TenantThreadLocal.getSystemUser();
        List<SelectItem> selectItemList = plain.getSelectItems();
        AtomicReference<Boolean> flag = new AtomicReference(false);
        selectItemList.forEach((selectItem) -> {
            if (selectItem.toString().contains("TENANT_CODE")) {
                flag.set(true);
            }

        });
        // 判断租户code是否存在

        if (systemUser != null && (Boolean)flag.get()) {
            whereSql.append("TENANT_CODE =");
            whereSql.append(systemUser.getTenantCode());
        }

        Expression where = plain.getWhere();
        Expression expression;
        if (where == null) {
            if (whereSql.length() > 0) {
                expression = CCJSqlParserUtil.parseCondExpression(whereSql.toString());
                plain.setWhere(expression);
            }
        } else {
            if (whereSql.length() > 0 && EmptyUtil.isEmpty(plain.getJoins())) {
                whereSql.append(" and ( ").append(where.toString()).append(" )");
            } else {
                whereSql = new StringBuffer();
                whereSql.append(where.toString());
            }

            expression = CCJSqlParserUtil.parseCondExpression(whereSql.toString());
            plain.setWhere(expression);
        }

        this.str(select, metaObject);

        return select.toString();
    }


    public String processInsert(Insert insert, BoundSql boundSql, MetaObject metaObject) {
        EyasFrameworkDto systemUser = (EyasFrameworkDto)TenantThreadLocal.getSystemUser();
        if (EmptyUtil.isNotEmpty(systemUser)) {

            Long tenantCode = systemUser.getTenantCode();
            List<Column> columnList = insert.getColumns();
            AtomicBoolean flag = new AtomicBoolean(false);
            columnList.forEach((column) -> {
                if ("TENANT_CODE".equals(column.toString())) {
                    flag.set(true);
                }

            });
            if (flag.get()) {
                int index = 0;

                for(int i = 0; i < columnList.size(); ++i) {
                    if ("TENANT_CODE".equals(((Column)columnList.get(i)).toString())) {
                        index = i;
                    }
                }

                if (insert.getItemsList() instanceof ExpressionList) {
                    ExpressionList expressionList = (ExpressionList)insert.getItemsList();
                    expressionList.getExpressions().set(index, new StringValue(String.valueOf(tenantCode)));
                } else if (insert.getItemsList() instanceof MultiExpressionList) {
                    MultiExpressionList multiExpressionList = (MultiExpressionList)insert.getItemsList();
                    Iterator var9 = multiExpressionList.getExprList().iterator();

                    while(var9.hasNext()) {
                        ExpressionList expression = (ExpressionList)var9.next();
                        expression.getExpressions().set(index, new StringValue(String.valueOf(tenantCode)));
                    }
                }

                List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
                List<ParameterMapping> parameterMappingList1 = new ArrayList();

                for(int i = 0; i < boundSql.getParameterMappings().size(); ++i) {
                    if (((ParameterMapping)boundSql.getParameterMappings().get(i)).toString().contains("tenantCode")) {
                        parameterMappingList1.add(parameterMappingList.get(i));
                    }
                }

                if (!EmptyUtil.dealListForceEmpty(parameterMappingList1)) {
                    parameterMappingList.removeAll(parameterMappingList1);
                }
                this.str(insert, metaObject);
                this.insertCount(boundSql.getParameterObject());

                return insert.toString();
            }
        }

        return boundSql.getSql();
    }

    private void str(Object object1, MetaObject metaObject){
        log.info("==>  Preparing: " + object1.toString());
        Object object = metaObject.getValue("delegate.parameterHandler.parameterObject");
        String json = GsonUtil.objectToJson(object);
        String paramString = "==> Parameters: ";
        log.info(paramString + json);
//        Map<String, Object> aa = GsonUtil.convertToMap(GsonUtil.objectToJson(object));
//
//
//        StringBuffer stringBuffer = new StringBuffer();
//        if (EmptyUtil.isNotEmpty(aa)) {
//            // 判断是一条数据还是多条数据
//            List<Object> bb = (List<Object>) aa.get("list");
//            if (EmptyUtil.isNotEmpty(bb)){
//                stringBuffer.append(aa.get("list") + ", ");
//            }else {
//                aa.forEach((k, v) -> {
//                    if (EmptyUtil.isNotEmpty(v)) {
//                        stringBuffer.append(k + ":" + v + ", ");
//                    }
//                });
//            }
//            if (EmptyUtil.isNotEmpty(stringBuffer)) {
//                log.info(paramString + stringBuffer.toString().substring(0, stringBuffer.length() - 2));
//            }
//        }
    }

    private void insertCount(Object object){
        Integer count = null;
        if (object instanceof Map){
            Map<String, List<Object>> aa = (Map) object;
            List<Object> ss = aa.get("list");
            count = ss.size();
        }else{
            count = 1;
        }
        log.info("<== Total: " + count);
    }

    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    public void setProperties(Properties properties) {
    }
}

