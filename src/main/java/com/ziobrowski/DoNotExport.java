package com.ziobrowski;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE,ElementType.TYPE_PARAMETER})
public @interface DoNotExport {
}

