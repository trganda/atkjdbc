package com.trganda.ruby;

import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Ruby {

    @Test
    public void rubyTest() throws ScriptException {
        String jrubyCode =
            "require \"java\"\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        engine.eval(jrubyCode);
    }
}
