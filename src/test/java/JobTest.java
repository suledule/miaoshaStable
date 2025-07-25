//import org.junit.Test;
//
///**
// * @Description
// * @Author wuqiusheng
// * @Version 1.0
// * @Date 2025/7/11
// */
//public class JobTest {
//
//    private final static String className = "admJobService";
//    private final static String token = "2ca7c771-d806-4813-a5ef-7f465ae85959";
//
//
//    @Test
//    public void testSaveJob() {
//        String param = "[{\"id\":\"\",\"folderId\":\"I4028b88197e2776a0197e3c6234f0034\",\"folderCode\":\"1\",\"groupName\":\"\",\"retry\":\"1\",\"retryInterval\":10,\"retryMaxnum\":10,\"cyclicMaxNum\":10,\"cyclicInterval\":20,\"cyclicExitCode\":\"code\",\"name\":\"测试111123\",\"alias\":\"测试111123\",\"desc\":\"22\",\"type\":\"Java\",\"msgType\":\"1\",\"agentServerIds\":\"\",\"agentServerName\":\"\",\"programName\":\"测试\",\"programPath\":\"11\",\"sourceSystem\":\"\",\"targetSystem\":\"\",\"exitCode\":\"1\",\"concurrency\":\"1\",\"version\":\"\",\"jobServer\":\"\",\"etlServerName\":\"\",\"sysCode\":\"987\",\"sysCodeName\":\"\",\"dicKeys\":[\"ODI\",\"01\"],\"dicKey\":\"ODI/01\",\"dicValue\":\"O层/eee\",\"projectName\":\"\",\"begin\":\"1900-01-01\",\"end\":\"9999-12-31\",\"dataSourceId\":\"\",\"flowName\":\"\",\"flowAlias\":\"\",\"calendarVersion\":\"20250710\",\"sysCodeAlias\":\"黑农信演示\",\"maxConnection\":null,\"priority\":-1,\"reRunStrategy\":\"1\",\"jobDebugStrategy\":\"0\",\"agencyFailover\":\"0\",\"forbiddenStrategy\":\"0\",\"jobFrequencyids\":\"Iff80808196a4dfb00197f1e40379009a,Iff80808196a4dfb00197f1e43c1a009c\",\"jobFrequencyNames\":\"\",\"runStrategy\":\"1\",\"jobResource\":100,\"jobCurrencyId\":\"Iff80808196a4dfb00197f1e4c95f00a0\"},[{\"id\":\"\",\"param\":{\"id\":\"ODATE_PARAMETER\",\"name\":\"$odate\",\"alias\":\"$odate\",\"paramValueType\":\"STRING\"},\"title\":\"$odate\",\"type\":\"0\",\"manual\":\"0\",\"format\":\"yyyyMMdd\",\"order\":3}]]";
//        HttpUtils.doPost(className, "saveJob", param, token);
//    }
//
//    @Test
//    public void testEditJob() {
//        String param = "[{\"id\":\"I4028b88197f74def0197f74def1a0000\",\"folderId\":\"I4028b88197e2776a0197e3c6234f0034\",\"folderCode\":\"1\",\"groupName\":\"\",\"retry\":\"1\",\"retryInterval\":10,\"retryMaxnum\":10,\"cyclicMaxNum\":10,\"cyclicInterval\":20,\"cyclicExitCode\":\"code\",\"name\":\"测试11112\",\"alias\":\"测试11112\",\"desc\":\"22\",\"type\":\"Java\",\"msgType\":\"1\",\"agentServerIds\":\"\",\"agentServerName\":\"\",\"programName\":\"测试\",\"programPath\":\"11\",\"sourceSystem\":\"\",\"targetSystem\":\"\",\"exitCode\":\"1\",\"concurrency\":\"1\",\"version\":\"\",\"jobServer\":\"\",\"etlServerName\":\"\",\"sysCode\":\"987\",\"sysCodeName\":\"\",\"dicKeys\":[\"ODI\",\"01\"],\"dicKey\":\"ODI/01\",\"dicValue\":\"O层/eee\",\"projectName\":\"\",\"begin\":\"1900-01-01\",\"end\":\"9999-12-31\",\"dataSourceId\":\"\",\"flowName\":\"\",\"flowAlias\":\"\",\"calendarVersion\":\"20250710\",\"sysCodeAlias\":\"黑农信演示\",\"maxConnection\":null,\"priority\":-1,\"reRunStrategy\":\"1\",\"jobDebugStrategy\":\"0\",\"agencyFailover\":\"0\",\"forbiddenStrategy\":\"0\",\"jobFrequencyids\":\"Iff80808196a4dfb00197f1e40379009a,Iff80808196a4dfb00197f1e43c1a009c\",\"jobFrequencyNames\":\"\",\"runStrategy\":\"1\",\"jobResource\":100,\"jobCurrencyId\":\"Iff80808196a4dfb00197f1e4c95f00a0\"},[{\"id\":\"\",\"param\":{\"id\":\"ODATE_PARAMETER\",\"name\":\"$odate\",\"alias\":\"$odate\",\"paramValueType\":\"STRING\"},\"title\":\"$odate\",\"type\":\"0\",\"manual\":\"0\",\"format\":\"yyyyMMdd\",\"order\":3}]]";
//        HttpUtils.doPost(className, "editJob", param, token);
//    }
//
//    @Test
//    public void testListForPage() {
//        String param = "[{\"name\":\"\",\"sysCodes\":[\"987\"],\"dicKey\":\"\",\"dicKeys\":[],\"limit\":20,\"start\":0}]";
//        HttpUtils.doPost(className, "listForPage", param, token);
//    }
//
//    @Test
//    public void testExistJobName() {
//        String param = "[\"6667\",\"987\",\"ODI/01\",\"Iff80808196a4dfb00197f2c90e3900b1\"]";
//        HttpUtils.doPost(className, "existJobName", param, token);
//    }
//
//    @Test
//    public void testExistJobAlias() {
//        String param = "[\"6667\",\"987\",\"ODI/01\",\"Iff80808196a4dfb00197f2c90e3900b1\"]";
//        HttpUtils.doPost(className, "existJobAlias", param, token);
//    }
//
//    @Test
//    public void testGetJobTree() {
//        String param = "[]";
//        HttpUtils.doPost(className, "getJobTree", param, token);
//    }
//
//    @Test
//    public void getJobAndParamById() {
//        String param = "[\"I4028b88197f74def0197f74def1a0000\"]";
//        HttpUtils.doPost(className, "getJobAndParamById", param, token);
//    }
//
//
//
//    public static void main(String[] args) {
//        double total = 400;
//        int year = 50;
//        double var = 1.02;
//        double amount = 12;
//        for (int i = 0; i < year; i++) {
//            total *= var;
//            total -= amount;
//            System.out.println("show:" + i + ":" + total);
//        }
//    }
//}
