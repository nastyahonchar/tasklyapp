package com.example.taskly.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.example.taskly.ui.common.CenteredScrollColumn
import com.example.taskly.ui.common.isTwoPaneLayout
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import taskly.shared.generated.resources.*

private data class OnboardingPage(
    val icon: ImageVector,
    val tag: StringResource,
    val title: StringResource,
    val description: StringResource,
)

private val pages = listOf(
    OnboardingPage(Icons.Default.CheckCircle, Res.string.onboarding_1_tag, Res.string.onboarding_1_title, Res.string.onboarding_1_desc),
    OnboardingPage(Icons.Default.Flag, Res.string.onboarding_4_tag, Res.string.onboarding_4_title, Res.string.onboarding_4_desc),
    OnboardingPage(Icons.Default.BarChart, Res.string.onboarding_2_tag, Res.string.onboarding_2_title, Res.string.onboarding_2_desc),
    OnboardingPage(Icons.Default.Devices, Res.string.onboarding_5_tag, Res.string.onboarding_5_title, Res.string.onboarding_5_desc),
    OnboardingPage(Icons.Default.DateRange, Res.string.onboarding_3_tag, Res.string.onboarding_3_title, Res.string.onboarding_3_desc),
)

private val log = Logger.withTag("Onboarding")

private val SinglePaneIllustrationSize = 200.dp
private val TwoPaneIllustrationSize = 280.dp
private val TwoPaneTextMaxWidth = 460.dp

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        log.d { "Page changed: ${pagerState.currentPage}" }
    }

    val onNext: () -> Unit = {
        if (pagerState.currentPage == pages.lastIndex) onFinish()
        else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
    }
    val onBack: () -> Unit = {
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
    }

    if (isTwoPaneLayout()) {
        OnboardingTwoPane(pagerState, onNext, onBack, onFinish)
    } else {
        OnboardingSinglePane(pagerState, onNext, onBack, onFinish)
    }
}

@Composable
private fun OnboardingSinglePane(
    pagerState: PagerState,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit,
) {
    val isLast = pagerState.currentPage == pages.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        OnboardingTopBar(showSkip = !isLast, onSkip = onFinish)

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                IllustrationPlate(pages[index], SinglePaneIllustrationSize)
                Spacer(Modifier.height(32.dp))
                PageText(pages[index], Alignment.CenterHorizontally, TextAlign.Center)
            }
        }

        PageIndicators(
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            arrangement = Arrangement.Center,
        )
        NavigationButtons(pagerState.currentPage, isLast, onBack, onNext)
    }
}

@Composable
private fun OnboardingTwoPane(
    pagerState: PagerState,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit,
) {
    val isLast = pagerState.currentPage == pages.lastIndex

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.secondaryContainer),
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    IllustrationPlate(
                        page = pages[index],
                        size = TwoPaneIllustrationSize,
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }

        CenteredScrollColumn(
            spacing = 0.dp,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 48.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = TwoPaneTextMaxWidth).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                OnboardingTopBar(showSkip = !isLast, onSkip = onFinish)

                AnimatedContent(
                    targetState = pages[pagerState.currentPage],
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.CenterStart,
                ) { page ->
                    PageText(page, Alignment.Start, TextAlign.Start)
                }

                PageIndicators(
                    currentPage = pagerState.currentPage,
                    arrangement = Arrangement.Start,
                )
                NavigationButtons(pagerState.currentPage, isLast, onBack, onNext)
            }
        }
    }
}

@Composable
private fun OnboardingTopBar(showSkip: Boolean, onSkip: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(Res.string.app_name),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        if (showSkip) {
            TextButton(onClick = onSkip) { Text(stringResource(Res.string.skip)) }
        }
    }
}

@Composable
private fun PageIndicators(
    currentPage: Int,
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    Row(modifier, horizontalArrangement = arrangement) {
        repeat(pages.size) { i ->
            val active = i == currentPage
            Box(
                Modifier
                    .padding(4.dp)
                    .height(6.dp)
                    .width(if (active) 24.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (active) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
            )
        }
    }
}

@Composable
private fun NavigationButtons(
    currentPage: Int,
    isLast: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentPage > 0) {
            TextButton(onClick = onBack) { Text(stringResource(Res.string.back)) }
        } else {
            Spacer(Modifier.width(1.dp))
        }

        Button(onClick = onNext, shape = RoundedCornerShape(24.dp)) {
            Text(stringResource(if (isLast) Res.string.get_started else Res.string.next))
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun IllustrationPlate(
    page: OnboardingPage,
    size: Dp,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(32.dp))
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            page.icon,
            contentDescription = null,
            modifier = Modifier.size(size * 0.48f),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun PageText(
    page: OnboardingPage,
    horizontalAlignment: Alignment.Horizontal,
    textAlign: TextAlign,
) {
    Column(horizontalAlignment = horizontalAlignment) {
        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
            Text(
                stringResource(page.tag),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = textAlign,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            stringResource(page.description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = textAlign,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}