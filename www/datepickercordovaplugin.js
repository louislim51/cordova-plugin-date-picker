var exec = require("cordova/exec");

module.exports = {
  coolMethod: function (arg0, success, error) {
    exec(success, error, "datepickercordovaplugin", "coolMethod", [arg0]);
  },

  printReceipt: function (arg0, success, error) {
    exec(success, error, "datepickercordovaplugin", "printReceipt", [arg0]);
  },
};
