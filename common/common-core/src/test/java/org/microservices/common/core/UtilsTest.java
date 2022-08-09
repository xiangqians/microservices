package org.microservices.common.core;

import org.microservices.common.core.util.JVMUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author xiangqian
 * @date 16:52:35 2022/03/19
 */
public class UtilsTest {

    public static void main(String[] args) throws Exception {
//        pid();
//        System.out.println(Integer.MIN_VALUE);

//        Set<Integer> set = Set.of(1, 23, 546, 7, 12, 3);
//        System.out.println(set);
//        List<Integer> list = set.stream().collect(Collectors.toList());
//        System.out.println(list);
//        Collections.sort(list, (o1, o2) -> {
//            return o1 - o2;
//        });
//        System.out.println(list);

//        ResourceResolver.getClassSet(null, "org.microservices.common.core")
//                .forEach(System.out::println);
//
//        List<Resource> resources = ResourceResolver.getResources(ResourceResolver.YML_RESOURCE_PATTERN, null);
//        for (Resource resource : resources) {
//            System.out.println(resource.getURL() + "," + new FileUrlResource(resource.getURL()));
//        }

    }


    public static void pid() {
        int pid = JVMUtils.pid();
        System.out.println(pid);
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
