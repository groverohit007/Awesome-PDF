package com.awesomepdf.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.Purchase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class BillingClientManager @Inject constructor(
    @ApplicationContext context: Context,
    private val entitlementManager: EntitlementManager
) {
    private val productDetailsMap = mutableMapOf<String, ProductDetails>()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener { result, purchases ->
            if (result.responseCode == BillingResponseCode.OK && purchases != null) {
                processPurchases(purchases)
            }
        }
        .build()

    private suspend fun ensureConnected() {
        if (billingClient.isReady) return
        suspendCancellableCoroutine { cont ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (result.responseCode == BillingResponseCode.OK) cont.resume(Unit)
                    else cont.resumeWithException(IllegalStateException(result.debugMessage))
                }

                override fun onBillingServiceDisconnected() = Unit
            })
        }
    }

    suspend fun launchPurchase(activity: Activity, productId: String): Result<Unit> = runCatching {
        ensureConnected()
        val type = if (productId == EntitlementManager.PRODUCT_LIFETIME) ProductType.INAPP else ProductType.SUBS
        val details = getProductDetails(productId, type) ?: error("Product not found: $productId")

        val productParamsBuilder = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)

        if (type == ProductType.SUBS) {
            val offerToken = details.subscriptionOfferDetails?.firstOrNull()?.offerToken
                ?: error("No offer token for $productId")
            productParamsBuilder.setOfferToken(offerToken)
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParamsBuilder.build()))
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, flowParams)
        if (billingResult.responseCode != BillingResponseCode.OK) {
            error(billingResult.debugMessage)
        }
    }

    suspend fun restorePurchases(): Result<Unit> = runCatching {
        ensureConnected()
        val subs = queryPurchases(ProductType.SUBS)
        val inApp = queryPurchases(ProductType.INAPP)
        processPurchases(subs + inApp)
    }

    suspend fun refreshEntitlement() {
        ensureConnected()
        val purchases = queryPurchases(ProductType.SUBS) + queryPurchases(ProductType.INAPP)
        processPurchases(purchases)
    }

    private suspend fun queryPurchases(type: String): List<Purchase> {
        return suspendCancellableCoroutine { cont ->
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(type).build()
            ) { result, purchases ->
                if (result.responseCode == BillingResponseCode.OK) cont.resume(purchases)
                else cont.resume(emptyList())
            }
        }
    }

    private suspend fun getProductDetails(productId: String, type: String): ProductDetails? {
        productDetailsMap[productId]?.let { return it }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(type)
                        .build()
                )
            )
            .build()

        val details = suspendCancellableCoroutine<List<ProductDetails>> { cont ->
            billingClient.queryProductDetailsAsync(params) { result, list ->
                if (result.responseCode == BillingResponseCode.OK) cont.resume(list)
                else cont.resume(emptyList())
            }
        }.firstOrNull()

        if (details != null) productDetailsMap[productId] = details
        return details
    }

    private fun processPurchases(purchases: List<Purchase>) {
        val purchasedIds = purchases
            .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
            .flatMap { it.products }

        val activeProductId = when {
            purchasedIds.contains(EntitlementManager.PRODUCT_LIFETIME) -> EntitlementManager.PRODUCT_LIFETIME
            purchasedIds.contains(EntitlementManager.PRODUCT_YEARLY) -> EntitlementManager.PRODUCT_YEARLY
            purchasedIds.contains(EntitlementManager.PRODUCT_MONTHLY) -> EntitlementManager.PRODUCT_MONTHLY
            else -> null
        }
        entitlementManager.updateFromPurchase(activeProductId)

        purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED && !it.isAcknowledged }
            .forEach { purchase ->
                billingClient.acknowledgePurchase(
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                ) { }
            }
    }
}
