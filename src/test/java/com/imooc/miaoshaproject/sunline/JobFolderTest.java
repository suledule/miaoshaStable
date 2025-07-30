package com.imooc.miaoshaproject.sunline;

import org.junit.Test;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/3
 */
public class JobFolderTest {

    private final static String className = "admJobFolderService";
    private final static String token = "052c4cb7-f9eb-4ed0-8e2f-13c1783822d1";

    @Test
    public void testQuery() {
        //String param = "[{\"parentFolderId\":\"123\",\"keywords\":\"456\"}]";
        String param = "[{}]";
        //String param = "[{\"parentFolderId\":\"I4028b88197e2776a0197e3c6234f0034\"}]";
        //String param = "[{\"keywords\":\"目录3add\"}]";
        HttpUtils.doPost(className, "getFolderTree", param, token);
    }

    @Test
    public void testAddFolder() {
        String param = "[{\"id\":\"\",\"parentFolderId\":\"\",\"sysCode\":\"TEST111\",\"folderCode\":\"TEST1\",\"folderName\":\"TEST2\",\"folderType\":\"smart\",\"calendarVersion\":\"1\"}]";
        HttpUtils.doPost(className, "saveOrUpdateFolder", param, token);
    }

    @Test
    public void testUpdateFolder() {
        String param = "[{\"id\":\"I4028b8819821f662019821f662f00000\",\"parentFolderId\":\"\",\"sysCode\":\"TEST111\",\"folderCode\":\"TEST1\",\"folderName\":\"TEST2\",\"folderType\":\"smart\",\"calendarVersion\":\"1\"}]";
        HttpUtils.doPost(className, "saveOrUpdateFolder", param, token);
    }

    @Test
    public void testDeleteFolder() {
        String param = "[{\"id\":\"I4028b88197f373d10197f373d1f90000\"}]";
        HttpUtils.doPost(className, "deleteFolder", param, token);
    }

    @Test
    public void testGetProxyGroup() {
        String param = "[\"I4028b88197ce340d0197ce3466400001\"]";
        HttpUtils.doPost("jobService", "listForPage", param, token);
    }

    @Test
    public void testGetCalendarTree() {
        String param = "[]";
        HttpUtils.doPost("admCalendarService", "getCalendarTree", param, token);
    }

    @Test
    public void testDisPlatformAuthService() {
        String param = "[\"2\"]";
        HttpUtils.doPost("disPlatformAuthService", "getCalendarsByTenantId", param, token);
    }


}
