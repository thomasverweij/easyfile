<script>        
    import { timeSince } from './utils.js'
    import { downloadFile } from './api.js';
    import { tokenStore } from './store.js'


    export let bucketData;
    let currentFileMenu;

    let download = async (e) => {
        e.preventDefault();
        let id = e.currentTarget.dataset.id;
        let filename = e.currentTarget.dataset.filename;
        let blob = await downloadFile(id, $tokenStore)
            .catch(() => alert("could not download file"))
        let elemx = window.document.createElement('a');
        elemx.href = window.URL.createObjectURL(blob);
        elemx.download = filename;
        elemx.style.display = 'none';
        document.body.appendChild(elemx);
        elemx.click();
        document.body.removeChild(elemx);
    }

    let toggleMenu = (id) => {
        if(currentFileMenu === id) {currentFileMenu = undefined}
        else {currentFileMenu = id}
    }
</script>
<div id="fileList">
<table class:invisible={bucketData.files.length == 0}> 
    <thead>
        <tr>
        <th class="rownumber">#</th>
        <th>File</th>
        <th class="uploaded">Uploaded</th>
        </tr>
    </thead>
    <tbody>
        {#each bucketData.files as file, i}
        <tr>
        <th scope="row">{i + 1}</th>
        <td><a href="/" data-id="{file.id}" title="{file.fileName}" on:click|preventDefault="{() => toggleMenu(file.id)}">{file.fileName}</a></td>
        <td>{timeSince(file.createdDate)} ago</td>
        </tr>
        <tr class:activerow={currentFileMenu != file.id}>
            <td></td>
            <td colspan="2" class="filemenu">
                <a href="/" data-id="{file.id}" data-filename="{file.fileName}" on:click={download}>Download</a>
                &nbsp;&nbsp;
                <a href="/" data-id="{file.id}">Delete</a>
            </td>
        </tr>
        {/each}
    </tbody>
</table>
</div>
<style>
    #fileList {
        text-align: center;
    }
    .invisible {
        visibility: hidden;
    }

    .activerow {
        display: none;
        background: white;
    }

    .filemenu {
        padding-bottom: 20px;
    }

    table {
        width: 100%;
        text-align: left;
        table-layout: fixed;
    }

    td, th {
        /* max-width: 400px; */
        overflow: hidden;
        padding-left: 10px;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    tr {
        height: 30px;
        animation: slide-down .1s ease-out;
    }

    @keyframes slide-down {
        0% { opacity: 0; -webkit-transform: translateY(-50%); }   
        100% { opacity: 1; -webkit-transform: translateY(0); }
    }

    .rownumber {
        width: 30px;
    }

    .uploaded {
        width: 120px;
    }


</style>