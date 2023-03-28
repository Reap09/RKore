package org.mysticnetwork.rkore.utils;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import static org.bukkit.Bukkit.getServer;

public class ConsoleLogging {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
    private ConsoleLogging.LogType logType = ConsoleLogging.LogType.NORMAL;
    public ConsoleLogging setConsoleLog(ConsoleLogging.LogType logType) {
        this.logType = logType;
        return this;
    }
    public enum LogType {
        NORMAL, LOW, NONE;
    }
    private void log(int type, String message) {
        if (logType == ConsoleLogging.LogType.NONE || (logType == ConsoleLogging.LogType.LOW && type == 0))
            return;
        console.sendMessage(ColorUtils.translateColorCodes(message));
    }
}
