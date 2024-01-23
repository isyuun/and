package net.pettip.app.navi.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.viewmodel.WalkViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : SearchBox
 * @Date        : 2023-12-13
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.SearchBox
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(viewModel:WalkViewModel, modifier: Modifier, changeIsSearching:(Boolean)->Unit){

    val searchText by viewModel.searchText.collectAsState()

    CustomTextField(
        value = searchText,
        onValueChange = { viewModel.updateSearchText(it) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                viewModel.viewModelScope.launch {
                    viewModel.dailyLifeTimeLineListClear()
                    viewModel.getTimeLineList()
                    changeIsSearching(false)
                }
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        placeholder = { Text(text = stringResource(R.string.enter_search_term), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = design_intro_bg,
            unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = design_intro_bg.copy(alpha = 0.5f)
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp, letterSpacing = (-0.4).sp
        ),
        shape = RoundedCornerShape(4.dp),
        innerPadding = PaddingValues(start=16.dp),
        trailingIcon = {
            if (searchText != "") {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.clickable {
                        viewModel.updateSearchText("")
                        viewModel.viewModelScope.launch {
                            viewModel.dailyLifeTimeLineListClear()
                            viewModel.getTimeLineList()
                        }
                    }
                )
            }
        }
    )
}