const {defineConfig} = require('@vue/cli-service')

const {baseUrl} = require("./src/utils/testDataUtils")
require("events").EventEmitter.defaultMaxListeners=0;
module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        proxy: {
            '/uploadFile': {
                target: `${baseUrl}/work/correctDocx`,
                changeOrigin: true,
                pathRewrite: {'^/uploadFile': ''},
                ws: true
            },
            '/correctFile': {
                target: `${baseUrl}/work/correctFile`,
                changeOrigin: true,
                pathRewrite: {'^/correctFile': ''},
                ws: true
            },
            '/replace': {
                target: `${baseUrl}/work/HumbleSubstitution`,
                changeOrigin: true,
                pathRewrite: {'^/replace': ''},
                ws: true
            },
            '/login': {
                target: `${baseUrl}/user/login`,
                changeOrigin: true,
                pathRewrite: {'^/login': ''},
                ws: true
            },
            '/logout': {
                target: `${baseUrl}/user/logout`,
                changeOrigin: true,
                pathRewrite: {'^/logout': ''},
                ws: true
            },
            '/user': {
                target: `${baseUrl}/user/register`,
                changeOrigin: true,
                pathRewrite: {'^/user': ''},
                ws: true
            },
            '/test': {
                target: `${baseUrl}/user/test`,
                changeOrigin: true,
                pathRewrite: {'^/test': ''},
                ws: true
            },
            '/toPdf': {
                target: `${baseUrl}/work/FileToPdf`,
                changeOrigin: true,
                pathRewrite: {'^/toPdf': ''},
                ws: true
            },
            '/toDocx': {
                target: `${baseUrl}/work/FileToDocx`,
                changeOrigin: true,
                pathRewrite: {'^/toDocx': ''},//FileCopy
                ws: true
            },
            '/fileCopy': {
                target: `${baseUrl}/work/FileCopy`,
                changeOrigin: true,
                pathRewrite: {'^/fileCopy': ''},
                ws: true
            },
            '/fileBat': {
                target: `${baseUrl}/work/FileBat`,
                changeOrigin: true,
                pathRewrite: {'^/fileBat': ''},
                ws: true
            },
            '/register': {
                target: `${baseUrl}/user/register`,
                changeOrigin: true,
                pathRewrite: {'^/register': ''},
                ws: true
            },
            '/getFileList': {
                target: `${baseUrl}/docx/mydocx`,
                changeOrigin: true,
                pathRewrite: {'^/getFileList': ''},
                ws: true
            },
            '/getFile': {
                target: `${baseUrl}/docx/Selectdocx`,
                changeOrigin: true,
                pathRewrite: {'^/getFile': ''},
                ws: true
            },
            '/delFile': {
                target: `${baseUrl}/docx/DeleteDocx`,
                changeOrigin: true,
                pathRewrite: {'^/delFile': ''},
                ws: true
            },
            // '/uploadMyFile': {
            //     target: `${baseUrl}/docx/docxupload`,
            //     changeOrigin: true,
            //     pathRewrite: {'^/uploadMyFile': ''},
            //     ws: true
            // },
            '/download': {
                target: `${baseUrl}/docx/docxdownload`,
                changeOrigin: true,
                pathRewrite: {'^/download': ''},
                ws: true
            },
            '/getMyInfo': {
                target: `${baseUrl}/user/getmessage`,
                changeOrigin: true,
                pathRewrite: {'^/getMyInfo': ''},
                ws: true
            },
            '/updateMyInfo': {
                target: `${baseUrl}/user/updatemessage`,
                changeOrigin: true,
                pathRewrite: {'^/updateMyInfo': ''},
                ws: true
            },
            '/newProject': {
                target: `${baseUrl}/docx/AddFile`,
                changeOrigin: true,
                pathRewrite: {'^/newProject': ''},
                ws: true
            },
            '/getAllProject': {
                target: `${baseUrl}/docx/SelectAllFile`,
                changeOrigin: true,
                pathRewrite: {'^/getAllProject': ''},
                ws: true
            },
            '/getOneProject': {
                target: `${baseUrl}/docx/mydocx`,
                changeOrigin: true,
                pathRewrite: {'^/getOneProject': ''},
                ws: true
            },
            '/saveConfig': {
                target: `${baseUrl}/docx/UpdateParameter`,
                changeOrigin: true,
                pathRewrite: {'^/saveConfig': ''},
                ws: true
            },
            '/setSelect': {
                target: `${baseUrl}/docx/SetFileSession`,
                changeOrigin: true,
                pathRewrite: {'^/setSelect': ''},
                ws: true
            },
            '/getSelect': {
                target: `${baseUrl}/docx/GetFileSession`,
                changeOrigin: true,
                pathRewrite: {'^/getSelect': ''},
                ws: true
            },
            '/projectDownload': {
                target: `${baseUrl}/docx/docxdownload`,
                changeOrigin: true,
                pathRewrite: {'^/projectDownload': ''},
                ws: true
            },
            '/projectdeleteFile': {
                target: `${baseUrl}/docx/DeleteDocx`,
                changeOrigin: true,
                pathRewrite: {'^/projectdeleteFile': ''},
                ws: true
            },
            '/projectAddFile': {
                target: `${baseUrl}/docx/docxupload`,
                changeOrigin: true,
                pathRewrite: {'^/projectAddFile': ''},
                ws: true
            },
            '/deleteProject': {
                target: `${baseUrl}/docx/DelFile`,
                changeOrigin: true,
                pathRewrite: {'^/deleteProject': ''},
                ws: true
            },
            '/SelectProject': {
                target: `${baseUrl}/docx/SelectFile`,
                changeOrigin: true,
                pathRewrite: {'^/SelectProject': ''},
                ws: true
            },
            '/getSelectedProjectInfo': {
                target: `${baseUrl}/docx/RefreshSession`,
                changeOrigin: true,
                pathRewrite: {'^/getSelectedProjectInfo': ''},
                ws: true
            },
            '/getArticleContent': {
                target: `${baseUrl}/work/GetText`,
                changeOrigin: true,
                pathRewrite: {'^/getArticleContent': ''},
                ws: true
            },
            '/createReport': {
                target: `${baseUrl}/docx/ComReports`,
                changeOrigin: true,
                pathRewrite: {'^/createReport': ''},
                ws: true
            },
            '/backUpload': {
                target: `${baseUrl}/docx/BackUpload`,
                changeOrigin: true,
                pathRewrite: {'^/backUpload': ''},
                ws: true
            },
            '/toPdfZip': {
                target: `${baseUrl}/work/FileToPdfForZip`,
                changeOrigin: true,
                pathRewrite: {'^/toPdfZip': ''},
                ws: true
            },
            '/tpDocxZip': {
                target: `${baseUrl}/work/FileToDocxForZip`,
                changeOrigin: true,
                pathRewrite: {'^/tpDocxZip': ''},
                ws: true
            }
        }
    }
})
