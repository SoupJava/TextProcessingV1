const email = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/

const phone =/^[1][3,4,5,7,8][0-9]{9}$/

const name = /^[a-zA-Z_][a-zA-Z0-9_-]{5,15}$/

const password = /^[a-zA-Z][a-zA-Z0-9_.-]{8,16}$/

const account = /(^[1-9]\d{12}$)/

const emailReg = (msg) => {
    return email.test(String(msg))
}

const phoneReg = (msg) => {
    return phone.test(String(msg))
}

const nameReg = (msg) => {
    return name.test(String(msg))
}
const passwordReg = (msg) => {
    return password.test(String(msg))
}

const accountReg = (msg) => {
    return account.test(String(msg))
}


module.exports = {
    emailReg,
    phoneReg,
    nameReg,
    passwordReg,
    accountReg
}