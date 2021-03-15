var webpack = require('webpack');
config.plugins.push(
    new webpack.ProvidePlugin(
        {'window.SqlJs': 'sql.js/dist/sql-wasm.js'}
    )
)
