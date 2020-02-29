// 是否为手机号
export function isPhone (value) {
  value = trim(value)
  const re = /^1[3456789]\d{9}$/
  if (!re.test(value)) {
    return false
  }
  return true
}

export function formatDate(date) {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  return [year, month, day].map(formatNumber).join('-')
}

export function formatStandardDate(date) {
  var dateTmp = date.split("-")
  return dateTmp.map(formatNumber).join('-')
}

export function todayDate() {
  let date = new Date()
  return formatDate(date)
}

export function tomorrowDate() {
  var tomorrow = new Date()
  tomorrow.setTime(tomorrow.getTime() + 24*60*60*1000)
  return formatDate(tomorrow)
}

export function futureDate(days) {
  let date = new Date()
  date.setDate(date.getDate() + days);
  return formatDate(date)
}

export function toDateTime(date, time) {
  var dateTmp = date.split("-")
  var timeTmp = time.split(":")
  let newDate = new Date()
  newDate.setFullYear(dateTmp[0])
  newDate.setMonth(dateTmp[1] - 1)
  newDate.setDate(dateTmp[2])
  newDate.setHours(timeTmp[0])
  newDate.setMinutes(timeTmp[1])
  if (timeTmp.length > 2) {
    newDate.setSeconds(timeTmp[2])
  }
  return newDate
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

const trim = (ostr) => {
  if (!ostr) return ''
  if (!ostr.replace) return ostr
  return ostr.replace(/^\s+|\s+$/g, '')
}
