#include <jni.h>
#include <string>

#include "pre_utils.h"
#include "preprocessing.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hatsnative_DatasetFragment_lowercaseStringTest(JNIEnv* env, jobject) {
    std::string str = "Hi, this is a TEST MessaGe..!!";

    hats::Preprocessing preprocessing;
    return env->NewStringUTF(preprocessing.toLowercase(str).c_str());
}