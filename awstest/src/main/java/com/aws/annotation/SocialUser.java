package com.aws.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 적용 위치
@Retention(RetentionPolicy.RUNTIME) // 영향 범위
public @interface SocialUser {

}
