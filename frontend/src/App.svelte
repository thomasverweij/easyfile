<script>
	import CreateBucket from "./CreateBucket.svelte";
	import Bucket from "./Bucket.svelte"
	import BucketLogin from "./BucketLogin.svelte"
	import { keyStore, tokenStore } from './store.js';
	import Notification from "./Notification.svelte";
	import { parseJwt } from "./utils";

	let hash = window.location.hash.split('#')[1]
	let bucketId = hash || undefined;

	$: currentBucketId = $tokenStore ? parseJwt($tokenStore).sub : undefined;
	$: showCreateBucket = !currentBucketId && !hash
	$: showBucketLogin = (hash && !currentBucketId) || (hash && currentBucketId && currentBucketId != hash)

	tokenStore.subscribe(value => {
			localStorage.setItem("token", value);
		});

	keyStore.subscribe(value => {
		localStorage.setItem("key", value);
	});

</script>
<div id="wrapper">
	<main>
		<h2 class="title"><a href="/">EasyFile</a></h2>
		{#if showCreateBucket}
			<CreateBucket bind:bucketId={bucketId} />
		{:else if showBucketLogin}
			<BucketLogin bind:bucketId={bucketId} />
		{:else}
			<Bucket bucketId={bucketId} />
		{/if}
		<Notification />
	</main>
</div>
<style>
	:global(body) {
		/* background-color: rgb(233, 233, 233); */
		color:rgb(55, 55, 55);
		padding: 0;
		margin: 0;
        overflow-y: auto;
	}

	#wrapper {
		width: 100%;
		margin:0;
		padding: 0;
		overflow-x: hidden;
	}

	:global(input) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	:global(button) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	:global(a), :global(a:visited) {	
		color: inherit;
	}

	h2 {
		font-family: Didot;
		font-size: 40pt;
		text-align: center;
		color: rgb(36, 36, 36);
	}

	@media (max-width: 500px) {
		h2 {
			margin: 10px;
		}
	}

	a {
  		color:inherit;
  		text-decoration: none;
	}
	 
	main {
		max-width: 650px;
		margin: 0 auto;
		padding-bottom: 30px;
	}


	@media (max-width: 700px) {
		main {
			max-width: none;
			padding: 0 30px 10px 30px;
		}
	}
</style>