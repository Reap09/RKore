package org.mysticnetwork.rkore.placeholders;

import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.runnable.AutoClearLag;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import static org.mysticnetwork.rkore.runnable.AutoClearLag.countdown;

public class ClearLagPlaceholder extends PlaceholderHook {
    public int countdownTimeInt;
    private String intervalFormat = Settings.ClearLag.INTERVAL_FORMAT;

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

//        if (!identifier.equals("clearlag_time")) {
//            return null;
//        }
        if (identifier.equals("clearlag_time")) {
            return "works!";
        }
        return null;
//        String message = null;
//        int timeLeft = AutoClearLag.countdown;
//        if (timeLeft == -1) {
//            message = Settings.ClearLag.PLACEHOLDER_NO_TIME;
//        }
//        if (intervalFormat.equals("minutes")) {
//            countdownTimeInt = countdown / 60;
//            String minuteFormat = (countdownTimeInt == 60 ? "minute" : "minutes");
//            message = Settings.ClearLag.PLACEHOLDER_TIME
//                    .replace("{time}", countdownTimeInt + "")
//                    .replace("{time-format}", minuteFormat);
//        }
//        if (intervalFormat.equals("hours")) {
//            countdownTimeInt = countdown / 3600;
//            String hourFormat = (countdownTimeInt == 3600 ? "hour" : "hours");
//            message = Settings.ClearLag.PLACEHOLDER_TIME
//                    .replace("{time}", countdownTimeInt + "")
//                    .replace("{time-format}", hourFormat);
//        }
//        if (intervalFormat.equals("seconds")) {
//            countdownTimeInt = countdown;
//            String secondsFormat = (countdownTimeInt == 1 ? "second" : "seconds");
//            message = Settings.ClearLag.PLACEHOLDER_TIME
//                    .replace("{time}", countdownTimeInt + "")
//                    .replace("{time-format}", secondsFormat);
//        }
//
//        return message;
    }
}
