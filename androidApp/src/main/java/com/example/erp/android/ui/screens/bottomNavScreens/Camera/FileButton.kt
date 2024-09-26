package com.example.erp.android.ui.screens.bottomNavScreens.Camera

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.erp.android.R

//@SuppressLint("UnrememberedMutableState")
//@Composable
//fun FileButton(
//    onFileSelected: (String) -> Unit,
//    title: String,
//    modifier: Modifier = Modifier,
//    isEnable :Boolean = true,
//    painterResourceId:Int = R.drawable.account_not_found,
//) : String{
//
//    val context = LocalContext.current
//    var file : String? by remember { mutableStateOf("") }
//
//    var uploadResponse : String by remember {
//        mutableStateOf("")
//    }
//
//    var uploadStatus by remember {          // Loader for the Upload Icon
//        mutableStateOf(false)
//    }
//
//    var _uri by remember { mutableStateOf<Uri?>(null) }
//    val chooseFile =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
//        { uri ->
//            uri?.let {
//                _uri = uri
//            }
//        }
//
//
//    Row() {
//        TextButton(
//            onClick = {
//                chooseFile.launch("image/*")
//            },
//            modifier = modifier
//                .padding(10.dp)
//                .fillMaxWidth(0.8f)
//                .border(
//                    1.dp, color = Color.Black,
//                    shape = RectangleShape
//                ),
//            enabled = isEnable
//
//        ) {
//            if (_uri == null) {
//                Text(text = title)
//            } else {
//                val contentResolver = context.contentResolver
//                file = getFileName(contentResolver = contentResolver, uri = _uri!!)
//                file?.let { Text(text = it) }
//            }
//        }
////        IconButton(onClick = {
////            if (_uri == null) {
////                Toast.makeText(context, "Please select File", Toast.LENGTH_SHORT)
////                    .show()
////            }
////            else {
////                uploadStatus = true
//////                    upload(context, _uri.value!!, viewModal = UploadViewModel(id,finalToken), title)
//////                    onFileSelected(_uri.value.toString())
////                val uploadResp = upload(context, _uri!!, viewModal = uploadViewModel, name,messageViewModel)
////                if(uploadResp != null){
////                    uploadResponse = uploadResp
////                }
////
////                GlobalScope.launch {
////                    delay(2000) // 1 second delay
////                    uploadStatus = false
////                }
////
////            }
////
////        }, modifier = Modifier.align(CenterVertically))
////        {
////            if(uploadStatus){
////                Loader()
////            }else {
////                Image(
////                    painter = painterResource(painterResourceId),
////                    contentDescription = "Upload Content"
////                )
////            }
////        }
//    }
//    return uploadResponse
//}


@SuppressLint("UnrememberedMutableState")
@Composable
fun FileButton(
    onFileSelected: (Uri) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    painterResourceId: Int = R.drawable.account_not_found,
){
    val context = LocalContext.current
    var _uri by remember { mutableStateOf<Uri?>(null) }

    val chooseFile =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                _uri = uri
                onFileSelected(uri)  // Pass the selected Uri to the parent
            }
        }

    Row {
        TextButton(
            onClick = {
                chooseFile.launch("image/*")
            },
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(0.8f),
            enabled = isEnable
        ) {
            if (_uri == null) {
                androidx.compose.material3.Text(text = title, color = Color.Blue )
            } else {
                val contentResolver = context.contentResolver
                val fileName = getFileName(contentResolver = contentResolver, uri = _uri!!)
                fileName?.let { androidx.compose.material3.Text(text = it,color = Color.Black) }
            }
        }
    }
//    return _uri
}


fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.use { cursor ->
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (columnIndex != -1) {
                return cursor.getString(columnIndex)
            }
        }
    }
    return null
}