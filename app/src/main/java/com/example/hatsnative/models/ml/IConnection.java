package com.example.hatsnative.models.ml;

import java.util.Random;

public interface IConnection {
    static double init_weights(EKernelWeightInitializer kernelWeightInitializer) {
        double result = 0d;
        switch (kernelWeightInitializer) {
            case GLOROT_UNIFORM:
                result = glorot_uniform_weight_initializer();
            case GLOROT_NORMAL:
                result = glorot_normal_weight_initializer();
            case RANDOM:
                result = random_weight_initializer();
        }

        return result;
    }

    static double random_weight_initializer() {
        return Math.random();
    }

    static double glorot_uniform_weight_initializer() {
        int num_inputs = 150;
        int num_hidden = 1;

        double sd = Math.sqrt(6d / (num_inputs + num_hidden));

        Random random = new Random();
        return random.doubles(-sd, (sd+1)).findFirst().getAsDouble();
    }

    static double glorot_normal_weight_initializer() {
        int num_inputs = 150;
        int num_hidden = 1;

        double sd = Math.sqrt(2d / (num_inputs + num_hidden));

        Random random = new Random();
        return random.nextGaussian() * sd;
    }
}
