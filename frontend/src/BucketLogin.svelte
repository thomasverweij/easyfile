<script>
    import { getToken } from './api.js';
    import { tokenStore } from './store.js'
    import { notify } from './utils.js';

    export let bucketId;
    let password;


    async function login() {
        let response = await getToken(bucketId, password)
            .catch(() => notify("something went wrong"))
        if (response.status == 200) {
            tokenStore.set(response.token)
        } else if (response.status == 401) {
            notify("Access denied")
        }
        status = response.status
    }

</script>
<div id="login">
    <p>Bucket:</p>
    <p><code>{bucketId}</code></p>
    <p>Password:</p>
    <p>
        <input 
            type="password" 
            placeholder="password" 
            bind:value={password} 
            on:keyup|preventDefault={(e) => e.code == 'Enter' ? login() : false}
        />
        <button on:click={login} >Ok</button>
    </p>
</div>

<style>
#login {
    border-radius: 25px;
    text-align: center;
    width: 50%;
    margin: 0 auto;
    padding: 30px;
    border: 2px solid #e1e1e1;
    box-shadow: 10px 10px rgb(77, 77, 77);
}
</style>