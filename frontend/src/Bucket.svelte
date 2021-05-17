<script>
    import { onMount } from 'svelte';
    import FileList from './FileList.svelte';
    import { getBucket, uploadFile } from './api.js';
    import { tokenStore } from './store.js'
    export let bucketId;

    let files;
    let uploading;
    let bucketData = {};
    let token;
    let error;
    let time = new Date();

	$: creationDate = new Date(bucketData.createdDate || "")
	$: deletionDate = new Date(creationDate)
	$: _ = deletionDate.setDate(deletionDate.getDate() + 1)

	$: distance = deletionDate.getTime() - time.getTime();
    $: hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    $: minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    $: seconds = Math.floor((distance % (1000 * 60)) / 1000);

    tokenStore.subscribe(value => {
		token = value;
	});

    $: if(files) upload()

    let upload = async () => {
        uploading = true;
        let res = await uploadFile(files[0], token)
            .catch(() => alert("could not upload file"))
            .finally(() => uploading = false);
        if (res.id) {
            getBucket(token).then((r) => { bucketData = r});
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
        alert("Bucket link copied to clipboard")
    }

    let deleteBucket = () => {
        if (confirm("This will also delete the bucket contents. Proceed?")){
            alert("bucket deleted")
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
            } else {
                error = "An error occurred"
            }
        });
        document.body.ondragover = (e) => {e.preventDefault()};
        document.body.ondrop = dropHandler;

        const interval = setInterval(() => {
			time = new Date();
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
        <div class="options-item"><a href="/" on:click={deleteBucket}>Delete bucket ({hours}:{minutes}:{seconds})</a></div>
        <div class="options-item"><a href="/" on:click={logout}>Logout</a></div>
    </div>
    <div id="main">
        <p>
            <span class:invisible={!uploading}>uploading file...</span>        
        </p>
        <FileList bucketData={bucketData} />
        <p class:invisible={bucketData.files.length != 0}><i>Drag and drop your file here, or click 'Add file' to upload your file.</i></p>
        <input bind:files type="file" class="invisible" id="fileInput" name="fileInput" />
    </div>
{:else if error}
    <div id="main">
        <p>{error}</p>
        <a href="/" on:click={logout}>Logout</a>
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

	 
</style>
