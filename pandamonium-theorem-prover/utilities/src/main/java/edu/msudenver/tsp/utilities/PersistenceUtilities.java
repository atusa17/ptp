package edu.msudenver.tsp.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PersistenceUtilities {

    private static String[] getNullPropertyNames(final Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null
                        || propertyName.equals("id"))
                .toArray(String[]::new);
    }

    public static void copyNonNullProperties(final Object source, final Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
