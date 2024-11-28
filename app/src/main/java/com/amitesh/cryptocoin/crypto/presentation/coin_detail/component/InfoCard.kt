package com.amitesh.cryptocoin.crypto.presentation.coin_detail.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.amitesh.cryptocoin.R
import com.amitesh.cryptocoin.ui.theme.AppTheme

@Composable
fun CardInfo(
    title: String,
    formattedText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
){
    val defaultTextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = contentColor
    )
    Card(
        modifier = modifier
            .padding(8.dp)
            .shadow(
                elevation = 15.dp,
                shape = RectangleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,

            ),
        shape = RectangleShape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary

        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = contentColor
        )
    ) {
        AnimatedContent(targetState = icon,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            label = "IconAnimation"
            ) {icon ->
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(75.dp)
                    .padding(16.dp),
                tint = contentColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedContent(targetState = formattedText,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            label = "valueAnimation"
        ) {formattedText ->
            Text(
                text = formattedText,
                style = defaultTextStyle,
                modifier = Modifier.padding(horizontal = 16.dp)

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = defaultTextStyle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)

        )


    }
}

@PreviewLightDark
@Composable
private fun CardInfoPreview(){
    AppTheme { 
        CardInfo(
            title = "Title",
            formattedText = "$ 68,555.44",
            icon = ImageVector.vectorResource(id = R.drawable.dollar)
//            modifier = TODO(),
//            contentColor = TODO(),
//            formattedTextStyle = TODO(),
        )
    }
}