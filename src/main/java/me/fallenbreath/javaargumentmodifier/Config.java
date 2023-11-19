package me.fallenbreath.javaargumentmodifier;

import java.util.HashMap;
import java.util.Map;

public class Config
{
	public String javaExecutable = "java";
	public String targetJar = "minecraft_server.jar";
	public final Map<String, String> jvmArgs = new HashMap<>();
	public final Map<String, String> runArgs = new HashMap<>();
}
