package com.kingfu.sceneview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.kingfu.sceneview.ui.theme.SceneViewTheme
import io.github.sceneview.Scene
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.math.Position
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SceneViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ARScreen()
//                    ModelScreen()
                }
            }
        }
    }
}
//
//@Composable
//fun ModelScreen() {
//    val nodes = remember { mutableStateListOf<Node>() }
//    val coroutine = rememberCoroutineScope()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Scene(
//            modifier = Modifier.fillMaxSize(),
//            nodes = nodes,
//            onCreate = { sceneView ->
//                sceneView.apply {
//                    setLifecycle(lifecycle = lifecycle)
//
//                    // Apply your configuration
////                    sceneView.lifecycleScope.launchWhenCreated {
//                    coroutine.launch {
//                        val hdrFile = "environments/studio_small_09_2k.hdr"
////                        val hdrFile = "environments/HDR_040_Field.hdr"
////                        val hdrFile = "environments/white_back_ground.hdr"
//                        sceneView.loadHdrIndirectLight(fileLocation = hdrFile, specularFilter = true) {
//                            intensity(30_000f)
//                        }
//                        sceneView.loadHdrSkybox( fileLocation = hdrFile) {
//                            intensity(50_000f)
//                        }
//
//                        val model =
////                            sceneView.modelLoader.loadModel(fileLocation = "models/high_quality_shark_animation.glb")!!
//                            sceneView.modelLoader.loadModel(fileLocation = "models/high_detailed_flying_bird.glb")!!
//                        val modelNode = ModelNode(sceneView = sceneView, model = model).apply {
//                            transform(
//                                position = Position(z = -4.0f),
//                                rotation = Rotation(x = 15.0f)
//                            )
//                            scaleToUnitsCube(units = 2.0f)
//                        }
//                        sceneView.addChildNode(node = modelNode)
//                    }
//                }
//            }
//        )
//    }
//}


lateinit var modelNode: ArModelNode

@Composable
fun ARScreen() {
    val nodes = remember { mutableStateListOf<ArNode>() }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    var isEnableButton by remember { mutableStateOf(value = true) }

    Column {
        Box(modifier = Modifier.fillMaxSize()) {
            ARScene(
                modifier = Modifier.fillMaxSize(),
                nodes = nodes,
                planeRenderer = true,
                onCreate = { arSceneView ->
                    // Apply your configuration
                    modelNode = ArModelNode(engine = Engine.create()).apply {
                        coroutine.launch {
                            loadModelGlb(
                                context = context,
                                glbFileLocation = "models/high_detailed_flying_bird.glb",
                            ) {
                                arSceneView.planeRenderer.isVisible = true
                            }
                            onAnchorChanged = {
                                isEnableButton = false
                            }
                            arSceneView.addChild(child = modelNode)

                        }
                    }
                },


                onSessionCreate = { session ->
                    // Configure the ARCore session

                },
                onFrame = { arFrame ->
                    // Retrieve ARCore frame update

                },
                onTap = { hitResult ->
                    // User tapped in the AR view
                },
            )


            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    enabled = isEnableButton,
                    onClick = {
                        modelNode.anchor()
                    },
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    Text(text = "Place Model")
                }
            }
        }
    }
}





