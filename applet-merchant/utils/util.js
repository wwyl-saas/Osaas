const toDateTime = (date,time)=>{
  var dateTmp = date.split("-")
  var timeTmp = time.split(":")
  let newDate = new Date()
  newDate.setFullYear(dateTmp[0])
  newDate.setMonth(dateTmp[1] - 1)
  newDate.setDate(dateTmp[2])
  newDate.setHours(timeTmp[0])
  newDate.setMinutes(timeTmp[1])
  if(timeTmp.length>2){
    newDate.setSeconds(timeTmp[2])
  }
  return newDate
}

const toDateTime1 = (datetime) => {
  var date = '2015-03-05 17:59:00.0';
  date = date.substring(0, 19);
  date = date.replace(/-/g, '/'); 
  let newDate = new Date(date);
  return newDate
}

const toDate = (date) => {
  var dateTmp = date.split("-")
  let newDate = new Date()
  newDate.setFullYear(dateTmp[0])
  newDate.setMonth(dateTmp[1] - 1)
  newDate.setDate(dateTmp[2])
  return newDate
}


const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatDate = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  return [year, month, day].map(formatNumber).join('-')
}

const formatStandardDate = date => {
  var dateTmp = date.split("-")
  return dateTmp.map(formatNumber).join('-')
}

const todayDate = ()=>{
  let date=new Date()
  return formatDate(date)
}

const futureDate = days =>{
  let date = new Date()
  date.setDate(date.getDate() + days);
  return formatDate(date)
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

const mytoast = (text, icon) => {
  wx.showToast({
    title: text || '服务异常',
    icon: icon || 'succes',
    duration: 1000,
    mask: true
  })
}


// 返回值：arg1加上arg2的精确结果
function accAdd(arg1, arg2) {
  var r1, r2, m
  try {
    r1 = arg1.toString().split(".")[1].length
  } catch (e) {
    r1 = 0
  }
  try {
    r2 = arg2.toString().split(".")[1].length
  } catch (e) {
    r2 = 0
  }
  m = Math.pow(10, Math.max(r1, r2));
  return ((arg1 * m + arg2 * m) / m).toFixed(2)
}

// 返回值：arg1减去arg2的精确结果
function accSub(arg1, arg2) {
  var r1, r2, m, n
  try {
    r1 = arg1.toString().split(".")[1].length
  } catch (e) {
    r1 = 0
  }
  try {
    r2 = arg2.toString().split(".")[1].length
  } catch (e) {
    r2 = 0
  }
  m = Math.pow(10, Math.max(r1, r2))
  // 动态控制精度长度
  n = (r1 >= r2) ? r1 : r2
  return ((arg1 * m - arg2 * m) / m).toFixed(2)
}

module.exports = {
  formatTime: formatTime,
  formatDate: formatDate,
  mytoast: mytoast,
  todayDate: todayDate,
  futureDate: futureDate,
  toDateTime: toDateTime,
  toDateTime1: toDateTime1,
  toDate: toDate,
  formatStandardDate: formatStandardDate,
  add:accAdd,
  sub:accSub
}