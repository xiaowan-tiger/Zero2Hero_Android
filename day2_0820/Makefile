# 目标可执行文件名
TARGET     := temp_daemon
SRC        := temp_daemon.c

# 编译器与常用标志
CC         := $(TOOLCHAIN)/bin/aarch64-linux-android21-clang
LDFLAGS    := 
LDLIBS     := -llog -landroid

# 默认目标
all: $(TARGET)

# 真正的链接规则
$(TARGET): $(SRC)
	$(CC) $< -o $@ $(LDFLAGS) $(LDLIBS)

# 清理
clean:
	rm -f $(TARGET)

# 可选：把产物推到手机里（需 adb 已在 PATH 且设备已连接）
install: $(TARGET)
	adb push $(TARGET) /data/local/tmp/

.PHONY: all clean install
