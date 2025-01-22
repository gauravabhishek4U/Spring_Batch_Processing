package com.batchProcessing.config;

import com.batchProcessing.model.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository,
                       JobCompletionNotificationImpl listener,
                       Step steps) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step steps(JobRepository jobRepository,
                      DataSourceTransactionManager dataSourceTransactionManager,
                      ItemReader<Product> itemReader,
                      ItemProcessor<Product, Product> itemProcessor,
                      ItemWriter<Product> itemWriter) {

        return new StepBuilder("jobStep", jobRepository)
                .<Product,Product>chunk(5, dataSourceTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    // implementing itemReader
    @Bean
    public FlatFileItemReader<Product> reader() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("itemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names("productId", "title", "description", "price", "discount")
                .targetType(Product.class)
                .build();
    }

    // implementing itemProcessor
    @Bean
    public ItemProcessor<Product, Product> itemProcessor() {
        return new CustomItemProcessor();
    }

    //implementing itemWriter
    @Bean
    public ItemWriter<Product> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("Insert into products(productId, title, description, price, discount, discounted_price) values(:productId, :title, :description, :price, :discount, :discounted_price)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

}
