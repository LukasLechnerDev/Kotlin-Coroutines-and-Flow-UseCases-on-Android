package com.lukaslechner.coroutineusecasesonandroid.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityUsecasesBinding

class UseCaseActivity : BaseActivity() {

    private val useCaseCategory by lazy {
        intent.getParcelableExtra<UseCaseCategory>(
            EXTRA_USE_CASE_CATEGORY
        )!!
    }

    private lateinit var binding: ActivityUsecasesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsecasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private val onUseCaseClickListener: (UseCase) -> Unit = { clickedUseCase ->
        startActivity(Intent(applicationContext, clickedUseCase.targetActivity))
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter =
                UseCaseAdapter(
                    useCaseCategory,
                    onUseCaseClickListener
                )
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@UseCaseActivity)
            addItemDecoration(initItemDecoration())
        }
    }

    private fun initItemDecoration(): DividerItemDecoration {
        val itemDecorator =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.recyclerview_divider
            )!!
        )
        return itemDecorator
    }

    override fun getToolbarTitle() = useCaseCategory.categoryName

    companion object {

        private const val EXTRA_USE_CASE_CATEGORY = "EXTRA_USE_CASES"

        fun newIntent(context: Context, useCaseCategory: UseCaseCategory) =
            Intent(context, UseCaseActivity::class.java).apply {
                putExtra(EXTRA_USE_CASE_CATEGORY, useCaseCategory)
            }
    }
}