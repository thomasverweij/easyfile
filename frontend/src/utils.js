import { messageStore } from './store.js'

export function timeSince(date) {
    
    date = Date.parse(date)
    let seconds = Math.floor((new Date() - date) / 1000);
    
    let interval = seconds / 31536000;
    
    if (interval > 1) {
        return Math.floor(interval) + " years";
    }
    interval = seconds / 2592000;
    if (interval > 1) {
        return Math.floor(interval) + " months";
    }
    interval = seconds / 86400;
    if (interval > 1) {
        return Math.floor(interval) + " days";
    }
    interval = seconds / 3600;
    if (interval > 1) {
        return Math.floor(interval) + " hours";
    }
    interval = seconds / 60;
    if (interval > 1) {
        return Math.floor(interval) + " minutes";
    }
    return Math.max(Math.floor(seconds), 0) + " seconds";
}

export function deletionCountDown(now, date, days) {
    let deletionDate = new Date(date)
    
    deletionDate.setDate(deletionDate.getDate() + days)
    
    let distance = Math.max(deletionDate.getTime() - now.getTime(), 0)
    
    let time = [
        Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
        Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60)),
        Math.floor((distance % (1000 * 60)) / 1000)
    ].map(t => {
        return t.toLocaleString('en-US', {
            minimumIntegerDigits: 2,
            useGrouping: false
        })
    })
    return "(" + time[0] + ":" + time[1] + ":" + time[2] + ")"
}

export function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    
    return JSON.parse(jsonPayload);
}

export function notify(message) {
    messageStore.set({message})
}


export let importRawKey = async (p) => {
    let enc = new TextEncoder();
    return window.crypto.subtle.importKey(
        "raw", 
        enc.encode(p), 
        {name: "PBKDF2"}, 
        false, 
        ["deriveBits", "deriveKey"]
    );
}

export let importJwkKey = async (k) => {
    return await window.crypto.subtle.importKey(
        "jwk",
        k,
        { "name": "AES-GCM", "length": 256},
        true,
        [ "encrypt", "decrypt" ]
    );
}

export let getKey = async (p) => {
    let salt = window.crypto.getRandomValues(new Uint8Array(16));
    let keyMaterial = await importRawKey(p);
    let key = await window.crypto.subtle.deriveKey(
        {
            "name": "PBKDF2",
            salt: salt, 
            "iterations": 100000,
            "hash": "SHA-256"
        },
        keyMaterial,
        { "name": "AES-GCM", "length": 256},
        true,
        [ "encrypt", "decrypt" ]
    )
    
    let exp = await window.crypto.subtle.exportKey("jwk", key)

    return btoa(JSON.stringify(exp, null, " "))
}

// let iv = window.crypto.getRandomValues(new Uint8Array(12))
let iv = new Uint8Array([67, 14, 37, 134, 140, 179, 227, 60, 63, 170, 185, 165])

export let encryptData = async (d, k) => {
    return await window.crypto.subtle.encrypt({name: "AES-GCM", iv: iv}, k, d)
    .catch((e)=>console.log(e))
}

export let decryptData = async (d, k) => {
    return await window.crypto.subtle.decrypt({ name: "AES-GCM", iv: iv}, k, d)
        .catch((e)=>console.log(e))
}

const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

var lookup = new Uint8Array(256);

for (var i = 0; i < chars.length; i++) {
    lookup[chars.charCodeAt(i)] = i;
}

let b64encode = (arraybuffer) => {
    var bytes = new Uint8Array(arraybuffer),
    i, len = bytes.length, base64 = "";

    for (i = 0; i < len; i+=3) {
      base64 += chars[bytes[i] >> 2];
      base64 += chars[((bytes[i] & 3) << 4) | (bytes[i + 1] >> 4)];
      base64 += chars[((bytes[i + 1] & 15) << 2) | (bytes[i + 2] >> 6)];
      base64 += chars[bytes[i + 2] & 63];
    }

    if ((len % 3) === 2) {
      base64 = base64.substring(0, base64.length - 1) + "=";
    } else if (len % 3 === 1) {
      base64 = base64.substring(0, base64.length - 2) + "==";
    }

    return base64;
}

let b64decode = (base64) => {
    var bufferLength = base64.length * 0.75,
    len = base64.length, i, p = 0,
    encoded1, encoded2, encoded3, encoded4;

    if (base64[base64.length - 1] === "=") {
      bufferLength--;
      if (base64[base64.length - 2] === "=") {
        bufferLength--;
      }
    }

    var arraybuffer = new ArrayBuffer(bufferLength),
    bytes = new Uint8Array(arraybuffer);

    for (i = 0; i < len; i+=4) {
      encoded1 = lookup[base64.charCodeAt(i)];
      encoded2 = lookup[base64.charCodeAt(i+1)];
      encoded3 = lookup[base64.charCodeAt(i+2)];
      encoded4 = lookup[base64.charCodeAt(i+3)];

      bytes[p++] = (encoded1 << 2) | (encoded2 >> 4);
      bytes[p++] = ((encoded2 & 15) << 4) | (encoded3 >> 2);
      bytes[p++] = ((encoded3 & 3) << 6) | (encoded4 & 63);
    }

    return arraybuffer;
}


export let encryptText = async (d, k) => {
    let t = new TextEncoder().encode(d)
    let c = await encryptData(t, k)
    return b64encode(c)
}

export let decryptText = async (d, k) => {
    let t = b64decode(d)
    let c = await decryptData(t, k)
    let a = new TextDecoder().decode(c)
    return a
}

export let readFileEncrypted = (file, key) => {
    return new Promise((resolve, reject) => {
        let reader = new FileReader()
        reader.onload = () => {
          resolve(encryptData(reader.result, key))
        };
        reader.onerror = reject;
        reader.readAsArrayBuffer(file);
    })
}

export let encryptFile = async (file, key) => {
    let k = await importJwkKey(JSON.parse(atob(key)))
    return new File(
        [await readFileEncrypted(file, k)], 
        await encryptText(file.name, k), 
        {"type": file.type}
    )
}

export let decryptFile = async (buffer, key) => {
    let k = await importJwkKey(JSON.parse(atob(key)))
    return await decryptData(buffer, k) || "Decryption error"
}

export let decryptBucketData = async (bucket, key) => {
    let k = await importJwkKey(JSON.parse(atob(key)))
    bucket.files = await Promise.all(bucket.files.map(async (f) => {
            let filename = await decryptText(f.fileName, k)
            f.fileName = filename || "Decryption error"
            return f
        })
    )
    return bucket
}