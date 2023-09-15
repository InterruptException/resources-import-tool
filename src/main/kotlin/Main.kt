import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.platform.LocalWindowInfo

data class ResourceItemData(
    val fileName: String,
    val resourceName: String
)

@Composable
@Preview
fun App() {
    var sourceDir by remember { mutableStateOf("") }
    var projectDir by remember { mutableStateOf("") }
    var projectResourceDir by remember { mutableStateOf("") }
    val fileItems = mutableStateListOf<ResourceItemData>()

    MaterialTheme {
        Column(modifier = Modifier.padding(10.dp),verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DirectorySelector("Source Directory:", sourceDir, { sourceDir = it })
            DirectorySelector("Project Directory:", sourceDir, { sourceDir = it })
            DirectorySelector("Project Resource Directory:", sourceDir, { sourceDir = it })
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().border(1.dp, Color.Black), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    ResourceItem(ResourceItemData("foo", "bar"), {});
                }
                itemsIndexed(fileItems, key = {_,b-> b.fileName }) {i,item->
                    ResourceItem(item, onValueChange = {newItem->
                        val i = fileItems.indexOfFirst {oldItem->
                            oldItem.fileName == newItem.fileName
                        }
                        if (i != -1) {
                            fileItems[i] = newItem
                        }
                    })
                    if (i < fileItems.lastIndex) {
                        Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun ResourceItem(data: ResourceItemData, onValueChange: (ResourceItemData)->Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = data.fileName, modifier = Modifier.weight(1f))
        Divider(modifier = Modifier.width(1.dp).height(IntrinsicSize.Max))
        TextField(value = data.resourceName, onValueChange = {
            onValueChange(data.copy(resourceName = it))
        }, modifier = Modifier.weight(1f))
    }
}

@Composable
fun DirectorySelector(label: String, path: String, onDirectoryChange: (String)->Unit) {

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = path, modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, Color.Gray))
        Spacer(modifier = Modifier.width(10.dp))
        Button(onClick = {
            java.awt.FileDialog(ComposeWindow()).isVisible = true
        }) {
            Text("Select Folder")
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {

        App()
    }
}
