package org.microservices.user.biz;

import lombok.Getter;
import org.microservices.common.core.enumeration.Enum;

/**
 * @author xiangqian
 * @date 14:58 2022/04/17
 */
public class ResourceResolverTest {

    public static void main(String[] args) throws Exception {
//        List<Resource> resources = ResourceResolver.getResources("**/*.xsd", null);
////        resources.forEach(System.out::println);
//        for (Resource resource : resources) {
//            BufferedReader bufferedReader = null;
//            try {
//                bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//                String line = null;
//                while ((line = bufferedReader.readLine()) != null) {
////                    if (line.contains("SpEL")) {
//                    if (line.contains("\"result")) {
//                        System.out.println(resource);
//                        break;
//                    }
//                }
//
//            } finally {
//                IOUtils.closeQuietly(bufferedReader);
//            }
//        }

//        Set<String> set = ResourceResolver.getPackageSet("org.microservices.**.support");
//        set.forEach(System.out::println);


        java.lang.Enum e = (java.lang.Enum) Enum.valueOf(EnumTest.class, 1);
        System.out.println(e);
    }

    @Getter
    public static enum EnumTest implements Enum<Integer> {
        TEST0(0, "test0"),
        TEST1(1, "test1"),
        TEST2(2, "test2"),
        TEST3(3, "test3"),
        ;

        private final Integer value;
        private final String description;

        EnumTest(int value, String description) {
            this.value = value;
            this.description = description;
        }

    }


}
