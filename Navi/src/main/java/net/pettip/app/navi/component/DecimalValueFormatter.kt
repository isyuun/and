package net.pettip.app.navi.component

import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.formatter.ValueFormatter
import java.text.DecimalFormat

/**
 * @Project     : PetTip-Android
 * @FileName    : DecimalValueFormatter
 * @Date        : 2024-04-12
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.DecimalValueFormatter
 */
class DecimalValueFormatter : ValueFormatter {

 private val decimalFormat = DecimalFormat("#.#")
 override fun formatValue(value: Float, chartValues: ChartValues): CharSequence {
  return decimalFormat.format(value.toDouble())
 }
}