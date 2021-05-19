<script>
    import { onMount } from 'svelte';
    import FileList from './FileList.svelte';
    import { getBucket, uploadFile, deleteBucket } from './api.js';
    import { tokenStore, messageStore } from './store.js'
    import { deletionCountDown, notify } from './utils.js'
    
    export let bucketId;
    let files;
    let uploading;
    let bucketData = {};
    let token;
    let now = new Date();
    let countdown = ""

    $: countdown = deletionCountDown(now, new Date(bucketData.createdDate || null), 1)

    tokenStore.subscribe(value => {
		token = value;
	});

    $: if(files) upload()

    let upload = async () => {
        uploading = files[0].name;
        let res = await uploadFile(files[0], token)
            .catch(() => notify("could not upload file"))
            .finally(() => uploading = false);
        if (res.id) {
            getBucket(token).then((r) => { 
                bucketData = r
                notify("File uploaded")
            });
        }
    } 

    let logout = () => {
        bucketData = undefined;
        token = undefined;
        bucketId = undefined;
        files = undefined;
        window.location = window.location.hash.split('#')[0];
        localStorage.removeItem('token');
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
            deleteBucket(token).then(() => {
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
        getBucket(token).then((r) => {
            bucketData = r;
            window.location.hash = r.id;
        }).catch((e) => {
            if(["401"].includes(e.message)) {
                logout()
            } else if (["404"].includes(e.message)) {
                notify("Bucket not found")
                logout()
            } else {
                notify("An error occurred")
                logout()
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
