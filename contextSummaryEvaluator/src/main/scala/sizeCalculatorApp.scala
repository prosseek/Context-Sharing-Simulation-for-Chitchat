package smcho

/**
 * Created by smcho on 9/16/15.
 */
object sizeCalculatorApp extends App {
  val testFilePathEmergency = "contextSummaryEvaluator/resources/jsonContexts/emergency/emergency.json"
  val sizeResultJsonFileName = "contextSummaryEvaluator/resources/jsonContexts/emergency/results.json"

  val v = new SizeCalculator(testFilePathEmergency)

  val r = v.getResultInJson(sizeResultJsonFileName)
}
