const path = require('path')

module.exports = {
    // 基本路径
    // publicPath: '/',

    // 输出文件目录
    // outputDir: 'dist',

    // assetsDir: '',

    // 环境配置
    devServer: {
        // host: 'localhost',
        // port: 8080,
        // 是否开启https
        https: false,
        // 编译完是否打开网页
        open: false,
        
        // 代理配置
        // proxy: {
        //     '^/api': {
        //         target: '<url>',
        //         ws: true,
        //         changeOrigin: true
        //     },
        //     '^/foo': {
        //         target: '<other_url>'
        //     }
        // }
    },

    // webpack配置
    chainWebpack: config => {
        // 配置路径别名
        config.resolve.alias
            .set('@', path.join(__dirname, 'src'))
            .set('@assets', path.join(__dirname, 'src/assets'))
            .set('@components', path.join(__dirname, 'src/components'))
            .set('@views', path.join(__dirname, 'src/views'))
    },

}