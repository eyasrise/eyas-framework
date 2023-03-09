package com.eyas.framework;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;

/**
 * @author Created by yixuan on 2019/9/12.
 */
public class TencentUploadUtil {

    public static String FILE_PATH = "https://eyas-ips-1259584691.cos.ap-chengdu.myqcloud.com";

    private static COSClient cosClient;

    static {
        String secretId = "AKIDSiw4b3L40xhDUcE7KUREqENuMbJJ745s";
        String secretKey = "jBPBk8bu8aTAh5LSWO1QaU8Yl4LSGFBj";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }

    public static void readFileList(){
        try {
            String bucket = "eyas-ips-1259584691";
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(bucket);
            // prefix 表示列出的 object 的 key 以 prefix 开始
            listObjectsRequest.setPrefix("");
            // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            listObjectsRequest.setDelimiter("/");
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            for (COSObjectSummary cosObjectSummary : objectListing.getObjectSummaries()) {
                // 对象的路径 key
                String key = cosObjectSummary.getKey();
                // 对象的 etag
                String etag = cosObjectSummary.getETag();
                // 对象的长度
                long fileSize = cosObjectSummary.getSize();
                // 对象的存储类型
                String storageClass = cosObjectSummary.getStorageClass();
                System.out.println("key:" + key + "; etag:" + etag + "; fileSize:" + fileSize + "; storageClass:" + storageClass);
            }
        } catch (CosClientException serverException) {
            serverException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
    }

    public static PutObjectResult uploadFile(File file,String filePath,String fileName){
        try {
            // 指定要上传到的存储桶
            String bucketName = "eyas-ips-1259584691";
            String key = filePath+"/"+fileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return putObjectResult;
        } catch (CosClientException serverException) {
            serverException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
        return null;
    }

    public static ObjectMetadata downloadFile(String key, String downloadPath){
        try{
            downloadPath = downloadPath + "\\" +key;
            // 指定对象所在的存储桶
            String bucketName = "eyas-ips-1259584691";
            // 指定要下载到的本地路径
            File downFile = new File(downloadPath);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
            return downObjectMeta;
        } catch (CosClientException serverException) {
            serverException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
        return null;
    }

    public static void deleteFile(String key){
        try {
            // 指定对象所在的存储桶
            String bucketName = "eyas-ips-1259584691";
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
    }

    public static void main(String[] args) {
        //1.创建文件
        // File file = new File("D:\\1212.txt");
        // System.out.println(TencentUploadUtil.uploadFile(file));
        // System.out.println(TencentUploadUtil.uploadFile(file).getDateStr());
        // System.out.println(JsonUtil.toJson(TencentUploadUtil.uploadFile(file)));
        // TencentUploadUtil.uploadFile(file);
        // TencentUploadUtil.deleteFile("1212.txt");
        TencentUploadUtil.downloadFile("1212.txt", "D:/data");
    }
}
