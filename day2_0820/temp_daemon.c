#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <android/log.h>
#include <sys/system_properties.h>



#define TAG "TempDaemon"
#define LOGI(fmt, ...) __android_log_print(ANDROID_LOG_INFO, TAG, fmt, ##__VA_ARGS__)
#define TEMP_PATH "/sys/class/thermal/thermal_zone0/temp"
#define LOG_PATH  "/data/temp.log"

int main(void)
{
    FILE *f;
    int temp;
    char value[16];

    while (1) {
        f = fopen(TEMP_PATH, "r");
        if (!f) { perror("open temp"); return 1; }
        fscanf(f, "%d", &temp);
        fclose(f);

        int celsius = temp / 1000;
        //LOGI("SoC temperature: %d°C", celsius);
        snprintf(value, sizeof(value),"%d", celsius);
        __system_property_set("persist.sys.temp.current",value);

        f = fopen(LOG_PATH, "a");
        if (f) {
            fprintf(f, "%d\n", celsius);
            fclose(f);
        }

        sleep(1);
    }
    return 0;
}
