package org.xiangqian.microservices.user.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xiangqian
 * @date 21:33 2023/08/18
 */
@EnableAspectJAutoProxy
@SpringBootApplication
public class UserBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserBizApplication.class, args);
    }

//    @Bean
//    public Validator validator() {
//        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
//                .configure()
//                .addProperty(HibernateValidatorConfiguration.FAIL_FAST, "true")
//                .buildValidatorFactory();
//        Validator validator = validatorFactory.getValidator();
//        return validator;
//    }
//
//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
//        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
//        methodValidationPostProcessor.setValidator(validator);
//        return methodValidationPostProcessor;
//    }
//@Bean
//public Validator validator() {
//    return Validation.buildDefaultValidatorFactory().getValidator();
//}

}


