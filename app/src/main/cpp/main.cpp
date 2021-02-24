#include <jni.h>
#include <string>
#include <fstream>
#include <vector>

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
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__))

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
    off_t assetLength = AAsset_getLength(asset);
    LOGI("File size: %d\n", assetLength);
    char *buffer = (char *) malloc(assetLength + 1);
    buffer[assetLength] = 0;
    int nbytes{0};
//    while( (nbytes = AAsset_read(asset, buffer, assetLength)) > 0) {
//        LOGD("%d", nbytes);
//        LOGD("%s", buffer);
//        AAsset_seek(asset, nbytes, SEEK_CUR);
//    }
    nbytes = AAsset_read(asset, buffer, assetLength);
//    LOGD("%d", nbytes);
    LOGD("%s", buffer);
    free(buffer);
//    AAsset_
//    LOGI("File size: %d\n", AAsset_get);
//    AAsset_seek(asset, nbytes, SEEK_CUR);
//    LOGD("%s", buffer);
//    int nb_read{0};
//
//    while (nb_read = AAsset_getRemainingLength(asset)) {
//        AAsset_read(asset, buffer, nb_read);
//        LOGD("%s", buffer);
//    }

    AAsset_close(asset);
    return env->NewStringUTF(file.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_hatsnative_fragments_HomeFragment_loadCsvData(JNIEnv* env, jobject thisObject, jobject csv_data) {
//    jclass objectClass = env->GetObjectClass(csv_data);
//    jmethodID stringMethod = env->GetMethodID(env, objectClass, );
//
//    std::string data = env->GetStringUTFChars(csv_data, 0);
//    LOGD("%s", data.c_str());
}