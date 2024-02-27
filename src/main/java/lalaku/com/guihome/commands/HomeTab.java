package lalaku.com.guihome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lalaku.com.guihome.commands.HomeCommand.MAX_HOME;

public class HomeTab implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("set", "delete", "help"), new ArrayList<>());
        } else if (args.length == 2 && !args[0].equals("help")) {
            List<String> numOfHome = new ArrayList<>();
            for (int i = 1; i <= MAX_HOME; i++) {
                numOfHome.add(String.valueOf(i));
            }
            return StringUtil.copyPartialMatches(args[1], numOfHome, new ArrayList<>());
        }
        return new ArrayList<>();
    }
}
