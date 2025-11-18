const axios = require('axios')


const baseUrl = "http://127.0.0.1:12345"

/**
 * 存储
 * @param userName
 * @param password
 */
const  setUser = async (userName , password) =>{

    let userData = {
        userId : userName,
        password: password,
        username : null,
        userTel : null,
        userEmail : null,
        userAvatarUrl : null
    }
    return new Promise((resolve, reject) => {
        axios.post(`http://localhost:8080/login`,userData).then((res) => {
            resolve(res.data.msg)
        }).catch(err => {
            if(err) reject(err)
        })
    })
}

/**
 * 获取
 */
const getUser = async () => {
    return new Promise((resolve, reject) => {
        axios
            .post(`http://localhost:8080/test`)
            .then((res) => {
                if (res.data.data !== null) {
                    resolve(1);
                } else {
                    resolve(0);
                }
            })
            .catch((err) => {
                console.log(err);
                reject(err);
            });
    });
};


/**
 * 删除
 */
const delUser = async () => {
    try {
        const res = await axios.post('http://localhost:8080/logout');
        if (res.data.mscr === '成功') {
            // console.log(res.data.mscr);
            return true;
        } else {
            return false;
        }
    } catch (error) {
        console.error(error);
        return false;
    }
};

module.exports = {
    baseUrl,
    setUser,
    getUser,
    delUser
}

// TODO 获取用户凭证