package kr.sul.statuseffect.item.food

import org.bukkit.ChatColor
import org.bukkit.Material

// Enum의 고유 property인 name과 itemName 혼동 주의
enum class AllFoods(val material: Material, val itemName: String, val addHealth: Double, val addFoodLevel: Double, val additionalLore: List<String>? = null) {
    APPLE_CANNED(Material.GOLDEN_APPLE, "과일 통조림", 0.5, 1.5),
    CARROT_CANNED(Material.CARROT_ITEM, "당근 통조림", 0.5, 1.5),
    SARDINE_CANNED(Material.RAW_FISH, "정어리 통조림", 1.0, 2.5);

    // https://pjh3749.tistory.com/279 로 개선 여지 있음
    companion object {
        fun find(material: Material, itemName: String): AllFoods? {
            return values().find { it.material == material && it.itemName == ChatColor.stripColor(itemName) }
        }
    }
}