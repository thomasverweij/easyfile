<script>
    import * as tus from "tus-js-client";
    import { Keychain, encryptedSize } from 'wormhole-crypto'
    import { onMount } from 'svelte';
    import FileList from './FileList.svelte';
    import { getBucket, deleteBucket, PREFIX} from './api.js';
    import { tokenStore, keyStore } from './store.js'
    import { deletionCountDown, notify, decryptBucketData, getSalt } from './utils.js'
    
    export let bucketId;
    let files;
    let uploading;
    let bucketData = {};
    let now = new Date();
    let countdown = ""

    $: countdown = deletionCountDown(now, new Date(bucketData.createdDate || null), 1)

    $: if(files) upload()

    let upload = async () => {
        // if(files[0].size >= 524288000) {
        //     notify("Selected file size exceeds limit of 500Mb")
        //     return
        // } 
        uploading = files[0].name

        let fileStream = new Response(files[0]).body // || files[0].stream()
        let keychain = new Keychain($keyStore, getSalt(bucketId))
        let encryptedFileStream = await keychain.encryptStream(fileStream)
        let settings = {
            endpoint: __APP__.env.prefix + "/upload",
            retryDelays: [0, 3000, 5000, 10000, 20000],
            metadata: {
                filename: files[0].name,
                filetype: files[0].type
            },
            headers: {
                "Authorization": $tokenStore
            },
            uploadSize: encryptedSize(files[0].size),
            chunkSize: 9000 * 1000
        }

        let callbacks = {
                onError: (error) => {
                    console.log("Failed because: " + error)
                    uploading = false
                    notify("Could not upload file")
                },
                onProgress: (bytesUploaded, bytesTotal) => {
                    var percentage = (bytesUploaded / bytesTotal * 100).toFixed(2)
                    console.log(bytesUploaded, bytesTotal, percentage + "%")
                },
                onSuccess: async () => {
                    let bucket = await getBucket($tokenStore)
                    bucketData = await decryptBucketData(bucket, $keyStore)
                    uploading = false
                    notify("File uploaded")
                }
            }

        let upload = new tus.Upload(encryptedFileStream.getReader(), Object.assign(settings, callbacks))
        upload.start()

    } 

    let logout = () => {
        bucketData = undefined;
        bucketId = undefined;
        files = undefined;
        window.location = window.location.hash.split('#')[0];
        localStorage.removeItem('token');
        localStorage.removeItem('key');
    }

    let selectFile = (e) =>  {
        e.preventDefault();
        document.getElementById("fileInput").click()
    } 

    let copyLink = (e) => {
        e.preventDefault()
        var tempInput = document.createElement("input");
        tempInput.style = "position: absolute; left: -1000px; top: -1000px";
        tempInput.value = window.location.href;
        document.body.appendChild(tempInput);
        tempInput.select();
        document.execCommand("copy");
        document.body.removeChild(tempInput);
        notify("Bucket link copied to clipboard")
    }

    let deleteCurrentBucket = (e) => {
        e.preventDefault()
        if (confirm("This will also delete the bucket contents. Proceed?")){
            deleteBucket($tokenStore).then(() => {
                notify("Bucket deleted")
                logout()
            }).catch((e) => {
                console.log(e.message)
            })
        }
    }

    let dropHandler = (e) => {
        e.preventDefault();
        if (e.dataTransfer.items) {
            files = [e.dataTransfer.items[0].getAsFile()]
        } else {
            files = e.dataTransfer.files
        }
    }

    onMount(() => {
        getBucket($tokenStore)
            .then((b) => decryptBucketData(b, $keyStore))
            .then((b) => {
                bucketId = b.id
                bucketData = b;
                window.location.hash = b.id;
            }).catch((e) => {
                if(["401"].includes(e.message)) {
                    logout()
                } else if (["404"].includes(e.message)) {
                    notify("Bucket not found")
                    logout()
                } else {
                    notify("An error occurred")
                    console.log(e)
                }
            });
        document.body.ondragover = (e) => {e.preventDefault()};
        document.body.ondrop = dropHandler;

        const interval = setInterval(() => {
			now = new Date();
		}, 1000);

		return () => {
			clearInterval(interval);
		};
    });

</script>

{#if Object.keys(bucketData).length !== 0}
    <div class="options">
        <div class="options-item"><a href="/" on:click={selectFile}>Add file</a></div>
        <div class="options-item"><a href="/" on:click={copyLink}>Copy bucket link</a></div>
        <div class="options-item"><a href="/" on:click={deleteCurrentBucket}>Delete bucket <pre>{countdown}</pre></a></div>
        <div class="options-item"><a href="/" on:click={logout}>Logout</a></div>
    </div>
    <div id="main">
        <FileList bind:bucketData={bucketData} uploading={uploading}/>
        <input bind:files type="file" class="invisible" id="fileInput" name="fileInput" />
    </div>
{:else}
    <div id="main">
        <p>loading bucket...</p>
    </div>
{/if}



<style>
    .options {
        display: flex;
        flex-direction: row;
        margin-bottom: 50px;
    }
    
    @media (max-width: 500px) {
        .options {
            flex-direction: column;
        } 
        .options > .options-item {
            margin-top: 10px;
        }
    }

    .options-item {
        flex-grow: 1;
        text-align: center;
    }

    #main {
        text-align: center;
    }

    .invisible {
        visibility: hidden;
    }


    #fileInput {
        overflow: hidden;
    }

    pre {
        display: inline;
    }


	 
</style>
