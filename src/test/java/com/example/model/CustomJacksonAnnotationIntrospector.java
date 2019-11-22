package com.example.model;

import com.example.annotations.FieldName;
import com.example.annotations.FromPrimitive;
import com.example.annotations.IgnoredField;
import com.example.annotations.ToPrimitive;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public String[] findEnumValues(Class<?> enumType, Enum<?>[] enumValues, String[] names) {
        Map<String, String> overrides = Stream.of(ClassUtil.getDeclaredFields(enumType))
                .filter(it -> it.isEnumConstant() && it.getAnnotation(FieldName.class) != null)
                .collect(Collectors.toMap(
                        it -> it.getName(),
                        it -> it.getAnnotation(FieldName.class).value()
                ));

        if (overrides.isEmpty()) {
            return super.findEnumValues(enumType, enumValues, names);
        }

        for (int i = 0; i < enumValues.length; ++i) {
            names[i] = overrides.getOrDefault(enumValues[i].name(), names[i]);
        }
        return names;
    }

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        FieldName fieldName = a.getAnnotation(FieldName.class);
        if (fieldName != null) {
            return PropertyName.construct(fieldName.value());
        }

        return super.findNameForSerialization(a);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        FieldName fieldName = a.getAnnotation(FieldName.class);
        if (fieldName != null) {
            return PropertyName.construct(fieldName.value());
        }

        return super.findNameForDeserialization(a);
    }

    @Override
    protected boolean _isIgnorable(Annotated a) {
        return a.hasAnnotation(IgnoredField.class) || super._isIgnorable(a);
    }

    @Override
    public JsonCreator.Mode findCreatorAnnotation(MapperConfig<?> config, Annotated a) {
        if (a.hasAnnotation(FromPrimitive.class)) {
            return JsonCreator.Mode.DEFAULT;
        }
        return super.findCreatorAnnotation(config, a);
    }

    @Override
    public Boolean hasAsValue(Annotated a) {
        if (a.hasAnnotation(ToPrimitive.class)) {
            return true;
        }
        return super.hasAsValue(a);
    }
}
