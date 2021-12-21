
import 'dart:async';
import 'package:flutter/services.dart';

class QqwingSudoku {
  final List<int> grid;
  final List<int> solution;

  QqwingSudoku(this.grid, this.solution);
}

class Qqwing {
  static const MethodChannel _channel = MethodChannel('qqwing');

  /// generate sudoku with given difficulty (0 - 3)
  /// 0 -> Simple
  /// 1 -> Easy
  /// 2 -> Intermediate
  /// 3 -> Expert
  static Future<QqwingSudoku> generateSudoku(int difficulty) async {
    assert(0 <= difficulty && difficulty <= 3);
    var res = await _channel.invokeMethod("generateSudoku", {"index" : difficulty});
    return QqwingSudoku(res["sudoku"], res["solution"]);
  }
}
