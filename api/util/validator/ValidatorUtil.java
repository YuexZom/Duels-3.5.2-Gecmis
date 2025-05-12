/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.validator;

import com.google.common.collect.ImmutableList;
import me.realized.duels.util.validator.BiValidator;
import me.realized.duels.util.validator.TriValidator;
import me.realized.duels.util.validator.Validator;

public final class ValidatorUtil {
    @SafeVarargs
    public static <T> ImmutableList<Validator<T>> buildList(Validator<T> ... validators) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Validator<T> validator : validators) {
            if (!validator.shouldValidate()) continue;
            builder.add(validator);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T1, T2> ImmutableList<BiValidator<T1, T2>> buildList(BiValidator<T1, T2> ... biValidators) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (BiValidator<T1, T2> biValidator : biValidators) {
            if (!biValidator.shouldValidate()) continue;
            builder.add(biValidator);
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T1, T2, T3> ImmutableList<TriValidator<T1, T2, T3>> buildList(TriValidator<T1, T2, T3> ... triValidators) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (TriValidator<T1, T2, T3> triValidator : triValidators) {
            if (!triValidator.shouldValidate()) continue;
            builder.add(triValidator);
        }
        return builder.build();
    }

    public static <T> boolean validate(ImmutableList<Validator<T>> chain, T validated) {
        return chain.stream().allMatch(validator -> validator.validate(validated));
    }

    public static <T1, T2> boolean validate(ImmutableList<BiValidator<T1, T2>> chain, T1 first, T2 second) {
        return chain.stream().allMatch(validator -> validator.validate(first, second));
    }

    public static <T1, T2, T3> boolean validate(ImmutableList<TriValidator<T1, T2, T3>> chain, T1 first, T2 second, T3 third) {
        return chain.stream().allMatch(validator -> validator.validate(first, second, third));
    }

    private ValidatorUtil() {
    }
}

