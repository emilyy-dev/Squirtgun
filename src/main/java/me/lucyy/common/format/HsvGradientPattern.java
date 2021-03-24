package me.lucyy.common.format;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HsvGradientPattern implements FormatPattern {
	final Pattern pattern = Pattern.compile("\\{hsv:([A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{([A-Fa-f0-9]{2})<}");

	public static String fade(int hue1, int hue2, int sat, int val, String text, String formats) {

		StringBuilder output = new StringBuilder();

		int[] hues = TextFormatter.fade(text.length(), hue1, hue2);

		StringBuilder formatters = new StringBuilder();
		if (formats != null) {
			for (char character : formats.toLowerCase().toCharArray()) formatters.append("&").append(character);
		}

		for (int x = 0; x < text.length(); x++) {
			output.append(ChatColor.of(Color.getHSBColor(hues[x] / 255f, sat / 255f, val / 255f)).toString());
			output.append(formatters);
			output.append(text.charAt(x));
		}

		return output.toString();
	}

	@Override
	public String process(String in, String format) {
		Matcher matcher = pattern.matcher(in);
		while (matcher.find()) {
			int hue1 = Integer.parseInt(matcher.group(1).substring(0, 2), 16);
			int sat = Integer.parseInt(matcher.group(1).substring(2, 4), 16);
			int val = Integer.parseInt(matcher.group(1).substring(4, 6), 16);

			int hue2 = Integer.parseInt(matcher.group(4), 16);
			String text = matcher.group(3);
			String formats = format;
			if (formats == null) formats = matcher.group(2);


			in = in.replace(matcher.group(), fade(hue1, hue2, sat, val, text, formats));
		}
		return in;
	}
}
