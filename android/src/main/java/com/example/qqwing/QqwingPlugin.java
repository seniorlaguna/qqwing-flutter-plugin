package com.example.qqwing;

import androidx.annotation.NonNull;

import com.qqwing.Difficulty;
import com.qqwing.QQWing;

import java.util.ArrayList;
import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * QqwingPlugin
 */
public class QqwingPlugin implements FlutterPlugin, MethodCallHandler {

  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private QQWing generator;

  static Difficulty[] difficulties = new Difficulty[]{
          Difficulty.SIMPLE,
          Difficulty.EASY,
          Difficulty.INTERMEDIATE,
          Difficulty.EXPERT
  };

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "qqwing");
    channel.setMethodCallHandler(this);
    generator = new QQWing();
  }

  HashMap<String, int[]> generateSudoku(Difficulty difficulty) {
    do {
      generator.generatePuzzle();
      generator.solve();
    } while (generator.getDifficulty() == difficulty && generator.hasUniqueSolution());


    HashMap<String, int[]> sudoku = new HashMap<String, int[]>();
    sudoku.put("sudoku", generator.getPuzzle());
    sudoku.put("solution", generator.getSolution());
    return sudoku;
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("generateSudoku")) {
      int difficultyIndex = call.argument("index");
      Difficulty difficulty = difficulties[difficultyIndex];
      HashMap<String, int[]> sudoku = generateSudoku(difficulty);
      result.success(sudoku);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
