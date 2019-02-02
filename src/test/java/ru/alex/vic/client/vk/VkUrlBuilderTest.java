package ru.alex.vic.client.vk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.alex.vic.TestUtils;

import javax.inject.Inject;
import javax.inject.Provider;

public class VkUrlBuilderTest {

    @Inject
    private Provider<VkUrlBuilder> vkUrlBuilder;

    @Before
    public void setUp() {
        TestUtils.createInjector(this, VkUrlBuilder.class);
    }


    @Test
    public void test() {
        {
            final String url = vkUrlBuilder.get().getCountries().code("RU", "UA", "BY").offset(100).count(100).build();
            System.out.println("url = " + url);
            final String expected = "https://api.vk.com/method/database.getCountries?code=RU,UA,BY&offset=100&count=100&access_token=";
            Assert.assertEquals(expected,
                    url.substring(0, expected.length())
            );
        }
        {
            final String url = vkUrlBuilder.get().getCountries().code("BY", "RU").build();
            final String expected = "https://api.vk.com/method/database.getCountries?code=BY,RU&access_token=";
            Assert.assertEquals(expected,
                    url.substring(0, expected.length())
            );
        }

    }


}