#include <jni.h>
#include <string>
#include <fstream>

#include "pre_utils.h"
#include "config.h"
#include "preprocessing.h"

#include "android/asset_manager.h"
#include "android/asset_manager_jni.h"
#include "android/log.h"

namespace {
    const char* TAG = "MainActivityJNI";
} // namespace

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__))

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hatsnative_fragments_DatasetFragment_lowercaseStringTest(JNIEnv* env, jobject) {
    std::string str = "Hi, this is a TEST MessaGe..!!";

    hats::Preprocessing preprocessing;
    return env->NewStringUTF(preprocessing.toLowercase(str).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hatsnative_fragments_HomeFragment_predictNative(JNIEnv* env, jobject,
        jstring command, jobject assetManager) {
    std::string file = hats::files::SOURCE_DATASET_FILENAME;

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
    AAsset *asset = AAssetManager_open(mgr, file.c_str(), AASSET_MODE_UNKNOWN);
    size_t assetLength = AAsset_getLength(asset);
    char *buffer = (char *) malloc(assetLength + 1);
    AAsset_read(asset, buffer, assetLength);

    int nb_read{0};

    std::ifstream ifs;
    ifs.open(file, std::ios::in);
    std::string line;
    LOGD("%s", buffer);
    if(ifs.good()) {
        LOGD("%s", "good file");
    } else {
        LOGD("%s", "bad file");
    }
    while(std::getline(ifs, line)) {
        LOGD("%s", buffer);
    }


    return env->NewStringUTF(file.c_str());
}