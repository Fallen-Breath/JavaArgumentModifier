package me.fallenbreath.javaargumentmodifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class JavaArgumentModifier
{
	private static final String CONFIG_FILE_NAME = "java_argument_modifier.json";

	private static Config readConfig() throws IOException
	{
		File file = new File(CONFIG_FILE_NAME);
		if (!file.isFile())
		{
			System.out.printf("Created default config file at '%s'\n", CONFIG_FILE_NAME);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Files.write(file.toPath(), gson.toJson(new Config()).getBytes(StandardCharsets.UTF_8));
		}

		return new Gson().fromJson(
				new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8),
				Config.class
		);
	}

	private static List<String> replaceArgs(String what, List<String> args, Map<String, String> replacements)
	{
		List<String> newArgs = new ArrayList<>();
		for (String s : args)
		{
			String[] arg = new String[]{s};
			replacements.forEach((regex, replacement) -> {
				if (Pattern.compile(regex).matcher(arg[0]).find())
				{
					System.out.printf("Replacing %s args '%s' to '%s'\n", what, arg[0], replacement);
					arg[0] = replacement;
				}
			});
			if (arg[0] != null)
			{
				newArgs.add(arg[0]);
			}
		}
		return newArgs;
	}

	private static void launchChild(List<String> runArgs, Config config) throws IOException, InterruptedException
	{
		List<String> jvmArgs = new ArrayList<>(ManagementFactory.getRuntimeMXBean().getInputArguments());

		jvmArgs = replaceArgs("jvm", jvmArgs, config.jvmArgs);
		runArgs = replaceArgs("run", runArgs, config.runArgs);

		List<String> commands = new ArrayList<>();
		commands.add(config.javaExecutable);
		commands.addAll(jvmArgs);
		commands.add("-jar");
		commands.add(config.targetJar);
		commands.addAll(runArgs);

		System.out.printf("Launching child with %s\n", commands);
		Process process =new ProcessBuilder(commands.toArray(new String[0])).
				inheritIO().
				start();
		process.waitFor();
	}

	public static void main(String[] args) throws Exception
	{
		Config config = readConfig();
		launchChild(Arrays.asList(args), config);
	}
}