package com.anasini.nuvodo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anasini.nuvodo.ui.theme.EmeraldPrimary
import com.anasini.nuvodo.ui.theme.LocalExtraColors

@Composable
fun SlotIndicator(
    activeCount: Int,
    maxSlots: Int = 6,
    modifier: Modifier = Modifier
) {
    val extraColors = LocalExtraColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, extraColors.cardBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DAILY FOCUS SLOTS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.2.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (activeCount >= maxSlots) "6/6 (CAP REACHED)" else "$activeCount of $maxSlots Active",
                style = MaterialTheme.typography.labelMedium,
                color = if (activeCount >= maxSlots) MaterialTheme.colorScheme.error else EmeraldPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 6 Segmented Pill Bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 0 until maxSlots) {
                val isFilled = i < activeCount
                val animatedColor by animateColorAsState(
                    targetValue = if (isFilled) EmeraldPrimary else extraColors.cardBorder,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "slotColor_$i"
                )
                val animatedScale by animateFloatAsState(
                    targetValue = if (isFilled) 1f else 0.95f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "slotScale_$i"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .scale(animatedScale)
                        .clip(RoundedCornerShape(4.dp))
                        .background(animatedColor)
                )
            }
        }
    }
}
