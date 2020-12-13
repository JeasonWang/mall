import Cookies from "js-cookie";
import request from '@/utils/request'

const SupportKey='supportKey';
export function getSupport() {
  return Cookies.get(SupportKey)
}

export function setSupport(isSupport) {
  return Cookies.set(SupportKey, isSupport,{ expires: 3 })
}

export function setCookie(key,value,expires) {
  return Cookies.set(key, value,{ expires: expires})
}

export function getCookie(key) {
  return Cookies.get(key)
}

export function removeCookie(key) {
  return Cookies.remove(key)
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/admin/captchaImage',
    method: 'get'
  })
}
